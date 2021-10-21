package com.mrluo.cloud.modules.app.service.impl;

import com.mrluo.cloud.modules.app.model.entity.Column;
import com.mrluo.cloud.modules.app.mapper.ColumnMapper;
import com.mrluo.cloud.modules.app.service.IColumnService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 新闻栏目表(Column)表服务实现类
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
@Transactional(rollbackFor = {Exception.class})
@Service("columnService")
public class ColumnServiceImpl extends ServiceImpl<ColumnMapper, Column> implements IColumnService {
}