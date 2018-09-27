/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.test;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.locator.BaseLocator;

import java.util.List;

/**
 * @author Matthew Lohbihler
 */
public class Test32 {
    public static void main(String[] args) throws Exception {
    	String commPortId = "COM3";
    	int baudRate = 9600;
    	int flowControlIn = 0;
		int flowControlOut = 0; 
		int dataBits = 8;
		int stopBits = 1;
		int parity = 0;
    	
    	TestSerialPortWrapper wrapper = new TestSerialPortWrapper(commPortId, baudRate, flowControlIn, flowControlOut, dataBits, stopBits, parity);

        ModbusMaster master = new ModbusFactory().createRtuMaster(wrapper);
        master.init();

        System.out.println(master.testSlaveNode(1));

        // Define the point locator.
        BaseLocator<Number> loc = BaseLocator.holdingRegister(1, 1, DataType.TWO_BYTE_INT_UNSIGNED);

        // Set the point value
//        master.setValue(loc, 5);

        // Get the point value
//        System.out.println("結果：" + master.getValue(loc));


//        List<Integer> slaveIds = master.scanForSlaveNodes();
//        System.out.println("slaveIds: " + slaveIds.size());
//        new Thread(()->{
//            while(true) {
//                try {
//                    Instant nowInstant = Instant.now();
//                    Number number = master.getValue(loc);
//                    System.out.println("number:" + number);
//                    Instant instant = Instant.now();
//                    Duration duration = Duration.between(instant,nowInstant);
//                    System.out.println(duration.getNano()+",seconds:" + duration.getSeconds()+",millseconds:" );
//                } catch (ModbusTransportException e) {
//                    e.printStackTrace();
//                } catch (ErrorResponseException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    Thread.sleep(5000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        master.destroy();
    }
}
