package com.pancake.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.pancake.entity.component.Block;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pancake.entity.util.Const;
import com.pancake.util.MongoUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chao on 2017/12/19.
 */
public class BlockDao {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Logger logger = LoggerFactory.getLogger(BlockDao.class);

    private static class LazyHolder {
        private static final BlockDao INSTANCE = new BlockDao();
    }
    private BlockDao(){}
    public static BlockDao getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * 保存 block 到数据库中
     * @param block
     * @param collectionName
     * @return
     */
    public boolean upSert(Block block, String collectionName) {
        MongoCollection<Document> collection = MongoUtil.getCollection(collectionName);
        // 如果集合不存在，则创建唯一索引
        if (!MongoUtil.collectionExists(collectionName)) {
            Document index = new Document("blockId", 0);
            collection.createIndex(index, new IndexOptions().unique(true));
        }

        Document document = Document.parse(block.toString());
        Bson filter = Filters.eq("blockId", block.getBlockId());
        Bson update = new Document("$set", document);
        UpdateOptions options = new UpdateOptions().upsert(true);
        UpdateResult updateResult = null;
        try {
            updateResult = collection.updateOne(filter, update, options);
        } catch (com.mongodb.MongoWriteException e) {
            logger.warn(e.getMessage());
        }

        return updateResult != null && updateResult.wasAcknowledged();
    }

    /**
     * 从集合 collectionName 获取所有 block
     * @param collectionName
     * @return
     */
    public List<Block> findAll(String collectionName) {
        List<String> list = MongoUtil.findAllSort(collectionName, "timestamp", Const.ASC);
        List<Block> blockList = new ArrayList<Block>();
        for (String blockJson : list) {
            try {
                blockList.add(objectMapper.readValue(blockJson, Block.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return blockList;
    }
}
