package ru.cu.controller.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import ru.cu.dto.ArticleCommentDto;
import ru.cu.service.ArticleCommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleCommentWebSocketController {

    private final ArticleCommentService articleCommentService;

    @MessageMapping("/comments/{articleId}/load")
    @SendTo("/topic/comments/{articleId}")
    public List<ArticleCommentDto> loadComments(@DestinationVariable Long articleId) {
        return articleCommentService.findByArticleId(articleId);
    }

    @MessageMapping("/comments/{articleId}/create")
    @SendTo("/topic/comments/{articleId}")
    public List<ArticleCommentDto> createComment(ArticleCommentDto commentDto) {
        articleCommentService.create(commentDto);
        return articleCommentService.findByArticleId(commentDto.getArticleId());
    }

    @MessageMapping("/comments/{articleId}/delete/{commentId}")
    @SendTo("/topic/comments/{articleId}")
    public List<ArticleCommentDto> deleteComment(@DestinationVariable Long articleId,
                                                 @DestinationVariable Long commentId) {
        articleCommentService.delete(commentId);
        return articleCommentService.findByArticleId(articleId);
    }
}
