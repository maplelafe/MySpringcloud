package com.ffb.order_service.respository;

import com.ffb.order_service.dao.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {
}
