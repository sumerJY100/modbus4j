/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.test;

import com.serotonin.cdc.modbus4j.exception.IllegalDataAddressException;
import com.serotonin.cdc.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.*;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.serial.rtu.RtuSlave;

import java.util.Arrays;

/**
 * @author Matthew Lohbihler
 */
public class Test31 {
    public static void main(String[] args) throws Exception {
    	String commPortId = "COM3";
    	int baudRate = 9600;
    	int flowControlIn = 0;
		int flowControlOut = 0; 
		int dataBits = 8;
		int stopBits = 1;
		int parity = 0;
    	
    	TestSerialPortWrapper wrapper = new TestSerialPortWrapper(commPortId, baudRate, flowControlIn, flowControlOut, dataBits, stopBits, parity);

        ModbusSlaveSet rtuSlave  = new ModbusFactory().createRtuSlave(wrapper);
        rtuSlave.addProcessImage(getModscanProcessImage(1));
        new Thread(()-> {
            try {
                rtuSlave.start();
            } catch (com.serotonin.modbus4j.exception.ModbusInitException e) {
                e.printStackTrace();
            }
        });
        rtuSlave.start();
//        BasicProcessImage basicProcessImage = new BasicProcessImage(1);
//        basicProcessImage.setHoldingRegister(100, (short) 45);
//        rtuSlave.addProcessImage(basicProcessImage);
//        ProcessImage basicProcessImage = rtuSlave.getProcessImage(1);
        while (true) {
            synchronized (rtuSlave) {
                try {
                    rtuSlave.wait(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            for (ProcessImage processImage : rtuSlave.getProcessImages())
                try {
                    updateProcessImage((BasicProcessImage) processImage);
                } catch (IllegalDataAddressException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }


//        ModbusFactory modbusFactory = new ModbusFactory();
//        ModbusSlaveSet slave = modbusFactory.createRtuSlave()

    }


    public void createSalve(){
        //创建modbus工厂
        ModbusFactory modbusFactory = new ModbusFactory();
        //创建TCP服务端
        final ModbusSlaveSet salve = modbusFactory.createTcpSlave(false);
        //创建ASCII服务端
//		final ModbusSlaveSet salve = getAsciiSalve();
        //向过程影像区添加数据
        salve.addProcessImage(getModscanProcessImage(1));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    salve.start();
                } catch (com.serotonin.modbus4j.exception.ModbusInitException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            synchronized (salve) {
                try {
                    salve.wait(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            for (ProcessImage processImage : salve.getProcessImages())
                try {
                    updateProcessImage((BasicProcessImage) processImage);
                } catch (IllegalDataAddressException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }

    /**
     * @Description: 创建寄存器
     * @param slaveId
     * @return
     */
    public static BasicProcessImage getModscanProcessImage(int slaveId) {
        BasicProcessImage processImage = new BasicProcessImage(slaveId);
        processImage.setInvalidAddressValue(Short.MIN_VALUE);

        //创建可读写开关量区
        processImage.setCoil(0, true);
        processImage.setCoil(1, false);
        processImage.setCoil(2, true);
        processImage.setCoil(3, true);
        processImage.setCoil(5, true);
        processImage.setCoil(6, true);
        processImage.setCoil(7, true);
        processImage.setCoil(8, true);
        processImage.setCoil(9, true);

        //创建只读开关量区
        processImage.setInput(0, false);
        processImage.setInput(1, false);
        processImage.setInput(2, true);
        processImage.setInput(3, false);
        processImage.setInput(4, true);
        processImage.setInput(5, true);
        processImage.setInput(6, true);
        processImage.setInput(7, true);
        processImage.setInput(8, true);
        processImage.setInput(9, true);

        //创建模拟量保持寄存器
        processImage.setHoldingRegister(0, (short) 1);
        processImage.setHoldingRegister(1, (short) 10);
        processImage.setHoldingRegister(2, (short) 100);
        processImage.setHoldingRegister(3, (short) 1000);
        processImage.setHoldingRegister(4, (short) 10000);
        processImage.setHoldingRegister(5, (short) 10000);
        processImage.setHoldingRegister(6, (short) 10000);
        processImage.setHoldingRegister(7, (short) 10000);
        processImage.setHoldingRegister(8, (short) 10000);
        processImage.setHoldingRegister(9, (short) 10000);

        //创建模拟量只读寄存器
        processImage.setInputRegister(0, (short) 10000);
        processImage.setInputRegister(1, (short) 1000);
        processImage.setInputRegister(2, (short) 100);
        processImage.setInputRegister(3, (short) 10);
        processImage.setInputRegister(4, (short) 1);
        processImage.setInputRegister(5, (short) 1);
        processImage.setInputRegister(6, (short) 1);
        processImage.setInputRegister(7, (short) 1);
        processImage.setInputRegister(8, (short) 1);
        processImage.setInputRegister(9, (short) 1);

        processImage.addListener(new BasicProcessImageListener());

        return processImage;
    }

    /**
     * @Description: 客户端修改本地寄存器的数据
     * @author-lsh
     * @date 2017年9月13日 下午5:47:30
     */
    public static class BasicProcessImageListener implements ProcessImageListener {
        @Override
        public void coilWrite(int offset, boolean oldValue, boolean newValue) {
            System.out.println("Coil at " + offset + " was set from " + oldValue + " to " + newValue);
        }

        @Override
        public void holdingRegisterWrite(int offset, short oldValue, short newValue) {
            // Add a small delay to the processing.
            //            try {
            //                Thread.sleep(500);
            //            }
            //            catch (InterruptedException e) {
            //                // no op
            //            }
            System.out.println("HR at " + offset + " was set from " + oldValue + " to " + newValue);
        }
    }

    /**
     * @Description: 更新寄存器的数据
     * @param processImage
     * @throws IllegalDataAddressException
     */
    static void updateProcessImage(BasicProcessImage processImage) throws IllegalDataAddressException {
        try {
            processImage.setInput(0, !processImage.getInput(0));
        } catch (com.serotonin.modbus4j.exception.IllegalDataAddressException e) {
            e.printStackTrace();
        }
        try {
            processImage.setInput(1, !processImage.getInput(1));
        } catch (com.serotonin.modbus4j.exception.IllegalDataAddressException e) {
            e.printStackTrace();
        }
        processImage.setHoldingRegister(1, (short) 3);
//	        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 20, DataType.FOUR_BYTE_FLOAT, ir1Value += 0.01);
        //
//	        short hr1Value = processImage.getNumeric(RegisterRange.HOLDING_REGISTER, 80, DataType.TWO_BYTE_BCD)
//	                .shortValue();
//	        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 80, DataType.TWO_BYTE_BCD, hr1Value + 1);
    }

    public void StringTest(){
        String a = "abc123456789";
        char[] charArray = a.toCharArray();
        System.err.println(Arrays.toString(charArray));
    }

}
