package com.mrluo.cloud.modules.app.controller;


import com.mrluo.cloud.modules.app.service.IArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 文章表(Article)表控制层
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
@RestController
@RequestMapping("article")
public class ArticleController {
    /**
     * 服务对象
     */
    @Resource
    private IArticleService articleService;


}