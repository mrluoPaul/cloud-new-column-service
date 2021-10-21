package com.mrluo.cloud.modules.app.controller;

import com.mrluo.cloud.modules.app.service.IColumnService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 新闻栏目表(Column)表控制层
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
@RestController
@RequestMapping("column")
public class ColumnController {
    /**
     * 服务对象
     */
    @Resource
    private IColumnService columnService;



}