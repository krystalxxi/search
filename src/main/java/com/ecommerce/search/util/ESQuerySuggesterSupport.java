package com.ecommerce.search.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.PhraseSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import co.elastic.clients.elasticsearch.core.search.TermSuggestOption;
import com.ecommerce.search.exception.ElasticSearchSdkException;
import org.apache.commons.lang.math.RandomUtils;

import java.io.IOException;
import java.util.List;

public class ESQuerySuggesterSupport {
    private ElasticsearchClient elasticsearchClient;

    public ESQuerySuggesterSupport(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    /**
     * 单词纠错
     *
     * @param index
     * @param keyword
     */
    public List<TermSuggestOption> termSuggester(String index, String field, String keyword) throws ElasticSearchSdkException {
        if (index.isEmpty() || field.isEmpty() || keyword.isEmpty()) {
            throw new ElasticSearchSdkException("入参错误");
        }
        String suggestName = "suggest_term_" + RandomUtils.nextInt();
        try {
            SearchResponse<String> response = elasticsearchClient.search(s -> s.index(index).suggest(su -> su.text(keyword).suggesters(suggestName, f -> f.term(t -> t.field(field)))), String.class);
            if (null != response && null != response.suggest() && null != response.suggest().get(suggestName)) {
                Suggestion<String> suggestion = response.suggest().get(suggestName).get(0);
                if (null != suggestion) {
                    return suggestion.term().options();
                }
            }
        } catch (IOException e) {
            throw new ElasticSearchSdkException("termSuggester查询报错:{}" + e.getMessage());
        }
        return null;
    }

    /**
     * 短语纠错
     *
     * @param index
     * @param phrase
     */
    public List<PhraseSuggestOption> phraseSuggester(String index, String field, String phrase) {
        if (index.isEmpty() || field.isEmpty() || phrase.isEmpty()) {
            throw new ElasticSearchSdkException("入参错误");
        }
        String suggestName = "suggest_phrase_" + RandomUtils.nextInt();
        try {
            SearchResponse<String> response = elasticsearchClient.search(s -> s.index(index).suggest(su -> su.text(phrase).suggesters(suggestName, p -> p.phrase(v -> v.field(field)))), String.class);
            if (null != response && null != response.suggest() && null != response.suggest().get(suggestName)) {
                Suggestion<String> suggestion = response.suggest().get(suggestName).get(0);
                if (null != suggestion) {
                    return suggestion.phrase().options();
                }
            }
        } catch (IOException e) {
            throw new ElasticSearchSdkException("phraseSuggester查询报错:{}" + e.getMessage());
        }
        return null;
    }

    /**
     * 补齐
     *
     * @param index
     * @param keyword
     */
    public List<CompletionSuggestOption<Object>> completableSuggester(String index, String field, String keyword) throws ElasticSearchSdkException {
        if (index.isEmpty() || field.isEmpty() || keyword.isEmpty()) {
            throw new ElasticSearchSdkException("入参错误");
        }
        String suggestName = "suggest_comp_" + RandomUtils.nextInt();
        try {
            SearchResponse<Object> response = elasticsearchClient.search(s -> s.index(index).suggest(su -> su.text(keyword).suggesters(suggestName, v -> v.completion(c -> c.field(field)))), Object.class);
            if (null != response && null != response.suggest() && null != response.suggest().get(suggestName)) {
                Suggestion<Object> suggestion = response.suggest().get(suggestName).get(0);
                if (null != suggestion) {
                    return suggestion.completion().options();
                }
            }
        } catch (IOException e) {
            throw new ElasticSearchSdkException("completableSuggester:{}" + e.getMessage());
        }
        return null;
    }

}
