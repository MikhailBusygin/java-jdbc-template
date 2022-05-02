package com.epam.izh.rd.online.auction.mappers;

import com.epam.izh.rd.online.auction.entity.Bid;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class BidMapper implements RowMapper<Bid> {

    @Override
    public Bid mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Bid(resultSet.getLong("bid_id"),
                resultSet.getObject("bid_date", LocalDate.class),
                resultSet.getDouble("bid_value"),
                resultSet.getLong("item_id"),
                resultSet.getLong("user_id"));
    }
}
