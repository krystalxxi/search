package com.ecommerce.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.ecommerce.search.util.ESQuerySuggesterSupport;
import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@Data
public class ElasticSearchConfig {
    // host ip 地址（集群）
    private String hosts;

    // es用户名
    private String userName;

    // es密码
    private String password;

    // es 请求方式
    private String scheme;

    // es 集群名称
    private String clusterName;

    // es连接超时时间
    private int connectTimeout;

    // es Socket连接超时时间
    private int socketTimeout;

    // es请求超时时间
    private int connectionRequestTimeout;

    // es 最大连接数
    private int maxConnectNum;

    private RestClient restClient;

    private ElasticsearchTransport transport;

    @PostConstruct
    public void init() {
        restClient = RestClient.builder(new HttpHost(hosts.substring(0, hosts.indexOf(":")), Integer.valueOf(hosts.substring(hosts.indexOf(":") + 1)), scheme)).build();
        transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    /**
     * 同步客户端
     *
     * @return
     */
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        if (null != transport) {
            return new ElasticsearchClient(transport);
        }
        return null;
    }

    @Bean
    public ESQuerySuggesterSupport esQuerySuggesterSupport(){
        return new ESQuerySuggesterSupport(elasticsearchClient());
    }

    /**
     * 异步客户端
     *
     * @return
     */
    @Bean
    public ElasticsearchAsyncClient asyncClient() {
        if (null != transport) {
            return new ElasticsearchAsyncClient(transport);
        }
        return null;
    }

}
