package com.mrluo.cloud.modules.app.controller;

import com.mrluo.cloud.common.ResponseData;
import com.mrluo.cloud.common.defs.NewsDefs;
import com.mrluo.cloud.modules.app.model.vo.ArticleVO;
import com.mrluo.cloud.modules.app.model.vo.ColumnVO;
import com.mrluo.cloud.modules.app.model.vo.TreeVO;
import com.mrluo.cloud.modules.app.service.IColumnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 新闻栏目表(Column)表控制层
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
@RestController
@Api(value = "栏目,文章管理")
@Validated
@RequestMapping(ColumnController.PREFIX_URI)
public class ColumnController {
    public static final String PREFIX_URI = NewsDefs.API_PREFIX_URI + "/column";
    /**
     * 服务对象
     */
    @Resource
    private IColumnService columnService;

    @PostMapping("add")
    @ApiOperation(value = "新增栏目或者文章", httpMethod = "POST")
    public ResponseData<Boolean> add(@RequestBody @Validated ColumnVO vo) {
        return ResponseData.success(columnService.add(vo));
    }

    @PostMapping("edit")
    @ApiOperation(value = "修改栏目或者文章", httpMethod = "POST")
    public ResponseData<Boolean> edit(@RequestBody @Validated ColumnVO vo) {
        return ResponseData.success(columnService.edit(vo));
    }

    @DeleteMapping("del/{id}")
    @ApiOperation(value = "删除栏目或者文章", httpMethod = "DELETE")
    public ResponseData<Boolean> del(@PathVariable @NotNull(message = "主键id不能为空") Long id) {
        return ResponseData.success(columnService.del(id));
    }

    @GetMapping("tree")
    @ApiOperation(value = "查询目录树形结构", httpMethod = "GET")
    public ResponseData<List<TreeVO>> tree() {
        return ResponseData.success(columnService.tree());
    }

    @GetMapping("detail")
    @ApiOperation(value = "查询文章详情", httpMethod = "GET")
    public ResponseData<ArticleVO> detail(@RequestParam @NotNull(message = "文章id不能为空") Long id) {
        return ResponseData.success(columnService.detail(id));
    }
}