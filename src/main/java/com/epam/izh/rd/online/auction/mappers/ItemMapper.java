package com.epam.izh.rd.online.auction.mappers;

import com.epam.izh.rd.online.auction.entity.Item;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class ItemMapper implements RowMapper<Item> {

    @Override
    public Item mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Item(resultSet.getLong("item_id"),
                resultSet.getDouble("bid_increment"),
                resultSet.getBoolean("buy_it_now"),
                resultSet.getString("description"),
                resultSet.getObject("start_date", LocalDate.class),
                resultSet.getDouble("start_price"),
                resultSet.getObject("stop_date", LocalDate.class),
                resultSet.getString("title"),
                resultSet.getLong("user_id"));
    }
}
