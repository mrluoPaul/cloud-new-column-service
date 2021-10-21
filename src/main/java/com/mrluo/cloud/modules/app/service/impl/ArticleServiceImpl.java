package com.mrluo.cloud.modules.app.service.impl;

import com.mrluo.cloud.modules.app.model.entity.Article;
import com.mrluo.cloud.modules.app.mapper.ArticleMapper;
import com.mrluo.cloud.modules.app.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2021-10-21 10:10:50
 */
@Transactional(rollbackFor = {Exception.class})
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
}