package com.wang.tx.txjpadb.dao;

import com.wang.tx.txjpadb.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wxl on 2018/9/22.
 *
 * @author wxl
 */
public interface CustomerDao extends JpaRepository<Customer,Long> {
}
