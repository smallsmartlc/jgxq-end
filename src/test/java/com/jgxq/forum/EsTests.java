package com.jgxq.forum;

import com.alibaba.fastjson.JSON;
import com.jgxq.common.res.UserRes;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LuCong
 * @since 2020-12-06
 **/
@SpringBootTest
public class EsTests {


    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    //测试索引的创建 Request
    @Test
    void testCreateIndex() throws IOException {
        //创建索引的请求
        CreateIndexRequest smart_index = new CreateIndexRequest("smart_index");
        //执行创建请求
        CreateIndexResponse response = client.indices().create(smart_index, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    //测试获取索引
    @Test
    void getCreateIndex() throws IOException {
        //获取索引的请求
        GetIndexRequest request = new GetIndexRequest("smart_index");
        //执行创建请求
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //测试删除索引
    @Test
    void delCreateIndex() throws IOException {
        //删除索引的请求
        DeleteIndexRequest request = new DeleteIndexRequest("smart_index");
        //执行创建请求
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }


    //测试添加文档
    @Test
    void addDocument() throws IOException {
        UserRes userRes = new UserRes(1, "hhh", "昵称", "头像", "重庆", 1);
        //创建索引对象
        IndexRequest request = new IndexRequest("smart_index");
        // 规则 PUT /smart_index/user/1
        request.id("1").timeout(TimeValue.timeValueSeconds(3));//链式
        request.source(JSON.toJSONString(userRes), XContentType.JSON);
        //发送请求

        IndexResponse index = client.index(request, RequestOptions.DEFAULT);

        System.out.println(index.getIndex());
        System.out.println(index.status());

    }

    //测试获取文档是否存在
    @Test
    void documentIsExist() throws IOException {
        GetRequest request = new GetRequest("smart_index", "1");
        //不获取返回值,效率更高
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");

        boolean exists = client.exists(request, RequestOptions.DEFAULT);

        System.out.println(exists);
    }

    //获取文档信息
    @Test
    void getDocument() throws IOException {
        GetRequest request = new GetRequest("smart_index", "1");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        Map<String, Object> source = response.getSource();
        System.out.println(response.getSourceAsString());
    }

    //测试更新
    @Test
    void updateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("smart_index", "1");

        UserRes userRes = new UserRes();
        userRes.setCity("贵州");
        request.doc(JSON.toJSONString(userRes), XContentType.JSON);

        client.update(request, RequestOptions.DEFAULT);
    }

    //删除
    @Test
    void delDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("smart_index", "1");

        client.delete(request, RequestOptions.DEFAULT);
    }

    //批量插入
    @Test
    void testBulkRequest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        List<UserRes> userList = new ArrayList<>();
        userList.add(new UserRes(1, "hhh", "昵称", "头像", "重庆", 1));
        userList.add(new UserRes(2, "hhh", "昵称", "头像", "重庆", 1));
        userList.add(new UserRes(3, "hhh", "昵称", "头像", "重庆", 1));
        userList.add(new UserRes(4, "hhh", "昵称", "头像", "重庆", 1));
        userList.add(new UserRes(5, "hhh", "昵称", "头像", "重庆", 1));

        for (int i = 0; i < userList.size(); i++) {
            bulkRequest.add(new IndexRequest("smart_index")
                    .id("" + (i + 1))
                    .source(JSON.toJSONString(userList.get(i)), XContentType.JSON));
        }

        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    // 查询
    @Test
    void search() throws IOException {

        SearchRequest searchRequest = new SearchRequest("smart_index");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("city.keyword", "重庆");
        sourceBuilder.query(queryBuilder);

        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println(JSON.toJSONString(response.getHits()));

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }
}
