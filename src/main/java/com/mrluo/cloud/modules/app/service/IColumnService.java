package com.mrluo.cloud.modules.app.service;

import com.mrluo.cloud.modules.app.model.entity.Column;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mrluo.cloud.modules.app.model.vo.ArticleVO;
import com.mrluo.cloud.modules.app.model.vo.ColumnVO;
import com.mrluo.cloud.modules.app.model.vo.TreeVO;

import java.util.List;

/**
 * 新闻栏目表(Column)表服务接口
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
public interface IColumnService extends IService<Column> {
    Boolean add(ColumnVO vo);

    Boolean edit(ColumnVO vo);

    Boolean del(Long id);

    List<TreeVO> tree();

    ArticleVO detail(Long id);
}