package com.wang.tx.txjpadb.service;

import com.wang.tx.txjpadb.dao.CustomerDao;
import com.wang.tx.txjpadb.domain.Customer;
import com.wang.tx.txjpadb.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wxl on 2018/9/22.
 *
 * @author wxl
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    @Qualifier("orderJdbcTemplate")
    private JdbcTemplate orderJdbcTemplate;

    private static final String SQL_UPDATE_DEPOSIT = "update t_user set deposit = deposit- ? where id = ?";

    private static final String SQL_CREATE_ORDER = "insert into t_order (customer_id,title,amount) values(?,?,?)";

    @Transactional
    public void createOrder(Order order){
        Long customerId = order.getCustomerId();
        Customer customer = customerDao.getOne(customerId);
        customer.setDeposit(customer.getDeposit() - order.getAmount());
        customerDao.save(customer);
        if(order.getTitle().contains("error1")){
            throw new RuntimeException("error1");
        }
        orderJdbcTemplate.update(SQL_CREATE_ORDER,order.getCustomerId(),order.getTitle(),order.getAmount());
        if(order.getTitle().contains("error2")){
            throw new RuntimeException("error2");
        }
    }


    public Map userInfo(String customerId){
        Customer customer = customerDao.getOne(Long.parseLong(customerId));
        List orders = orderJdbcTemplate.queryForList("select * from t_order where customer_id ="+customerId);
        Map r = new HashMap();
        r.put("customer",customer);
        r.put("orders",orders);
        return r;
    }




}
