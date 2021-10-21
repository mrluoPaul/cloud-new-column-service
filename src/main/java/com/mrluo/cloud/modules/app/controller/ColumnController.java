package com.mrluo.cloud.modules.app.controller;

import com.mrluo.cloud.common.ResponseData;
import com.mrluo.cloud.common.defs.NewsDefs;
import com.mrluo.cloud.modules.app.model.vo.ColumnVO;
import com.mrluo.cloud.modules.app.service.IColumnService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 新闻栏目表(Column)表控制层
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
@RestController
@RequestMapping(ColumnController.PREFIX_URI)
public class ColumnController {
    public static final String PREFIX_URI = NewsDefs.API_PREFIX_URI + "/column";
    /**
     * 服务对象
     */
    @Resource
    private IColumnService columnService;

    @PostMapping("add")
    @ApiOperation(value = "新增栏目")
    public ResponseData<Boolean> add(@RequestBody @Validated ColumnVO vo) {
        return ResponseData.success(columnService.add(vo));
    }
}