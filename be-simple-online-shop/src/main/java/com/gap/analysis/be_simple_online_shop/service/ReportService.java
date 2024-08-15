package com.gap.analysis.be_simple_online_shop.service;

import com.gap.analysis.be_simple_online_shop.entity.Order;
import com.gap.analysis.be_simple_online_shop.model.order.OrderReport;
import com.gap.analysis.be_simple_online_shop.repository.OrderRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    public byte[] generateOrderReport() throws Exception{
        List<OrderReport> orderList = orderRepository.findAll().stream().map((order -> {
            OrderReport orderReport = new OrderReport();
            orderReport.setOrderId(order.getOrderId());
            orderReport.setOrderCode(order.getOrderCode());
            orderReport.setQuantity(order.getQuantity());
            orderReport.setOrderDate(order.getOrderDate());
            orderReport.setCustomerName(order.getCustomer().getCustomerName());
            orderReport.setItemName(order.getItem().getItemName());
            orderReport.setTotalPrice(order.getTotalPrice());

            return  orderReport;
        })).collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orderList);

        //Load Template
        InputStream templateStream = resourceLoader.getResource("classpath:order.jrxml").getInputStream();

        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "CaseBTPN");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
