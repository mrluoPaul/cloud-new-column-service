package com.mrluo.cloud.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mrluo.cloud.common.exception.BusinessException;
import com.mrluo.cloud.common.exception.FailCodeEnum;
import com.mrluo.cloud.common.utils.localmap.NewsSecurityUtil;
import com.mrluo.cloud.modules.app.model.entity.Column;
import com.mrluo.cloud.modules.app.mapper.ColumnMapper;
import com.mrluo.cloud.modules.app.model.vo.ArticleVO;
import com.mrluo.cloud.modules.app.model.vo.ColumnVO;
import com.mrluo.cloud.modules.app.model.vo.TreeVO;
import com.mrluo.cloud.modules.app.service.IColumnService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 新闻栏目表(Column)表服务实现类
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */

@Service("columnService")
@Slf4j
public class ColumnServiceImpl extends ServiceImpl<ColumnMapper, Column> implements IColumnService {
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean add(ColumnVO vo) {
        Column column = new Column();
        String username = NewsSecurityUtil.getCurrentUser().get().getUsername();
        Date date = new Date();
        String name = vo.getName();
        Integer type = vo.getType();
        Long parentId = vo.getParentId();
        LambdaQueryWrapper<Column> wrapper = new LambdaQueryWrapper<>();
        if (type == 1) {
            wrapper.eq(Column::getName, name);
            wrapper.eq(Column::getParentId, parentId);
            Column one = this.getOne(wrapper);
            if (!Objects.isNull(one)) {
                throw new BusinessException(FailCodeEnum.COLUMN_NAME_REPEAT, FailCodeEnum.COLUMN_NAME_REPEAT.getMsg());
            }
        } else if (type == 2) {
            Column parentColumn = this.getById(parentId);
            if (Objects.isNull(parentColumn)) {
                throw new BusinessException(FailCodeEnum.COLUMN_OR_ARTICLE_IS_NULL, FailCodeEnum.COLUMN_OR_ARTICLE_IS_NULL.getMsg());
            }
            if (!name.equals(parentColumn.getName())) {
                throw new BusinessException(FailCodeEnum.COLUMN_NAME_IS_ERR, FailCodeEnum.COLUMN_NAME_IS_ERR.getMsg());
            }
        }
        column.setParentId(parentId);
        column.setType(type);
        column.setContent(vo.getContent());
        column.setCreatedUser(username);
        column.setUpdatedUser(username);
        column.setCreatedDate(date);
        column.setUpdatedDate(date);
        column.setName(name);
        return this.save(column);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean edit(ColumnVO vo) {
        String username = NewsSecurityUtil.getCurrentUser().get().getUsername();
        Long id = vo.getId();
        if (Objects.isNull(id)) {
            throw new BusinessException(FailCodeEnum.ID_IS_NULL, FailCodeEnum.ID_IS_NULL.getMsg());
        }
        String name = vo.getName();
        String content = vo.getContent();
        Column column = this.getById(id);
        if (Objects.isNull(column)) {
            throw new BusinessException(FailCodeEnum.COLUMN_OR_ARTICLE_IS_NULL, FailCodeEnum.COLUMN_OR_ARTICLE_IS_NULL.getMsg());
        }
        Date date = new Date();
        column.setContent(content);
        column.setName(name);
        column.setUpdatedDate(date);
        column.setUpdatedUser(username);
        return this.updateById(column);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean del(Long id) {
        Column column = this.getById(id);
        if (Objects.isNull(column)) {
            throw new BusinessException(FailCodeEnum.COLUMN_OR_ARTICLE_IS_NULL, FailCodeEnum.COLUMN_OR_ARTICLE_IS_NULL.getMsg());
        }
        return this.removeById(id);
    }

    @Override
    public List<TreeVO> tree() {
        List<TreeVO> finalList = new ArrayList<>();
        List<TreeVO> tree = baseMapper.tree();
        if (CollectionUtils.isEmpty(tree)) {
            return new ArrayList<>();
        }
        for (TreeVO parent : tree) {
            if (parent.getParentId() == 0) {
                finalList.add(parent);
            }
            for (TreeVO children : tree) {
                if (parent.getId().equals(children.getParentId())) {
                    parent.getChildren().add(children);
                }
            }
        }
        filter(finalList);
        return finalList;
    }

    @Override
    public ArticleVO detail(Long id) {
        ArticleVO vo = new ArticleVO();
        Column column = this.getById(id);
        if (Objects.isNull(column)) {
            throw new BusinessException(FailCodeEnum.COLUMN_OR_ARTICLE_IS_NULL, FailCodeEnum.COLUMN_OR_ARTICLE_IS_NULL.getMsg());
        }
        Long parentId = column.getParentId();
        Column parent = this.getById(parentId);
        if (!Objects.isNull(parent) && StringUtils.isNotEmpty(parent.getName())) {
            vo.setColumnName(parent.getName());
        }
        vo.setName(column.getName());
        vo.setContent(column.getContent());
        vo.setId(id);
        return vo;
    }

    private void filter(List<TreeVO> finalList) {
        for (TreeVO vo : finalList) {
            List<TreeVO> children = vo.getChildren();
            if (vo.getType() == 1) {
                children = children.stream().sorted(Comparator.comparing(TreeVO::getCreatedTime)).collect(Collectors.toList());
            } else if (vo.getType() == 2) {
                children = children.stream().sorted(Comparator.comparing(TreeVO::getUpdatedTime).reversed()).collect(Collectors.toList());
            }
            vo.setChildren(children);
            filter(children);
        }
    }
}