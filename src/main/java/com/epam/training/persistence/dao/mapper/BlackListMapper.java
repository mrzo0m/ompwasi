package com.epam.training.persistence.dao.mapper;

import com.epam.training.persistence.pojo.BlackList;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Oleg_Burshinov on 21.01.14.
 */
public class BlackListMapper implements ParameterizedRowMapper<BlackList> {
    @Override
    public BlackList mapRow(ResultSet resultSet, int i) throws SQLException {
        BlackList blackList = new BlackList();
        blackList.setUserId(resultSet.getInt("USER_ID"));
        blackList.setBlackUserId(resultSet.getInt("BLACK_USER_ID"));
        return blackList;
    }
}
