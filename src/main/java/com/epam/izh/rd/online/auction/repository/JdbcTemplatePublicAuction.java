package com.epam.izh.rd.online.auction.repository;

import com.epam.izh.rd.online.auction.entity.Bid;
import com.epam.izh.rd.online.auction.entity.Item;
import com.epam.izh.rd.online.auction.entity.User;
import com.epam.izh.rd.online.auction.mappers.BidMapper;
import com.epam.izh.rd.online.auction.mappers.ItemMapper;
import com.epam.izh.rd.online.auction.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JdbcTemplatePublicAuction implements PublicAuction {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private BidMapper bidMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Bid> getUserBids(long id) {
        return jdbcTemplate.query("SELECT * FROM bids WHERE user_id = ?", bidMapper, id);
    }

    @Override
    public List<Item> getUserItems(long id) {
        return jdbcTemplate.query("SELECT * FROM items WHERE user_id = ?", itemMapper, id);
    }

    @Override
    public Item getItemByName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM items WHERE title LIKE ?", itemMapper, name);
    }

    @Override
    public Item getItemByDescription(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM items WHERE description LIKE ?", itemMapper, name);
    }

    @Transactional
    @Override
    public Map<User, Double> getAvgItemCost() {
        String query = "SELECT user_id, AVG(start_price) FROM items GROUP BY user_id";
        List<Map<String, Object>> usersAvgItemCostList = jdbcTemplate.queryForList(query);
        Map<User, Double> avgItemCost = new HashMap<>();
        for (Map<String, Object> map : usersAvgItemCostList) {
            avgItemCost.put(
                    jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", userMapper, map.get("user_id")),
                    (Double) map.get("AVG(start_price)"));
        }
        return avgItemCost;
    }

    @Transactional
    @Override
    public Map<Item, Bid> getMaxBidsForEveryItem() {
        String query = "SELECT item_id, MAX(bid_value) FROM bids GROUP BY item_id";
        String itemQuery = "SELECT * FROM items WHERE item_id = ?";
        String bidQuery = "SELECT * FROM bids WHERE bid_value = ? AND item_id = ?";
        List<Map<String, Object>> itemsMaxBidValueList = jdbcTemplate.queryForList(query);
        Map<Item, Bid> maxBidsForEveryItem = new HashMap<>();
        for (Map<String, Object> map : itemsMaxBidValueList) {
            maxBidsForEveryItem.put(
                    jdbcTemplate.queryForObject(itemQuery, itemMapper, map.get("item_id")),
                    jdbcTemplate.queryForObject(bidQuery, bidMapper, map.get("MAX(bid_value)"), map.get("item_id")));
        }
        return maxBidsForEveryItem;
    }

    @Override
    public boolean createUser(User user) {
        return jdbcTemplate.update("INSERT INTO users VALUES (?, ?, ?, ?, ?)", user.getUserId(),
                user.getBillingAddress(), user.getFullName(), user.getLogin(), user.getPassword()) != 0;
    }

    @Override
    public boolean createItem(Item item) {
        return jdbcTemplate.update("INSERT INTO items VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", item.getItemId(),
                item.getBidIncrement(), item.getBuyItNow(), item.getDescription(), item.getStartDate(),
                item.getStartPrice(), item.getStopDate(), item.getTitle(), item.getUserId()) != 0;
    }

    @Override
    public boolean createBid(Bid bid) {
        return jdbcTemplate.update("INSERT INTO bids VALUES (?, ?, ?, ?, ?)", bid.getBidId(), bid.getBidDate(),
                bid.getBidValue(), bid.getItemId(), bid.getUserId()) != 0;
    }

    @Override
    public boolean deleteUserBids(long id) {
        return jdbcTemplate.update("DELETE FROM bids WHERE user_id = ?", id) != 0;
    }

    @Override
    public boolean doubleItemsStartPrice(long id) {
        return jdbcTemplate.update("UPDATE items SET start_price = start_price * 2 WHERE user_id = ?", id) != 0;
    }
}
