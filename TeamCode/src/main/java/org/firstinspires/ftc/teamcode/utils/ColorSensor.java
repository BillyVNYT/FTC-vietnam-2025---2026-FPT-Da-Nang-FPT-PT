package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.util.TypeConversion;

@I2cDeviceType
public class ColorSensor extends I2cDeviceSynchDevice<I2cDeviceSynch> {

    // Địa chỉ I2C mặc định của TCS34725 là 0x29
    public final static I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create7bit(0x29);

    public String detectBallColor() {
        int r = getRed();
        int g = getGreen();
        int b = getBlue();
        int c = getClear();

        // Tránh chia cho 0 nếu cảm biến bị lỗi
        if (c == 0) return "UNKNOWN";

        // Tính tỷ lệ phần trăm của từng màu
        double redRatio = (double) r / c;
        double greenRatio = (double) g / c;
        double blueRatio = (double) b / c;

        // Thuật toán so sánh
        if (greenRatio > redRatio && greenRatio > blueRatio) {
            return "GREEN"; // Bóng xanh
        } else if (redRatio > greenRatio && blueRatio > greenRatio) {
            return "PURPLE"; // Bóng tím (Đỏ và Xanh dương đều mạnh hơn Xanh lá)
        }

        return "NONE";
    }

    public enum Register {
        ENABLE(0x00),
        ATIME(0x01),
        CONTROL(0x0F), // Gain
        ID(0x12),
        CDATAL(0x14), // Clear channel low byte
        RDATAL(0x16), // Red low byte
        GDATAL(0x18), // Green low byte
        BDATAL(0x1A), // Blue low byte
        COMMAND_BIT(0x80); // Phải OR với thanh ghi khi ghi/đọc

        public final int bVal;
        Register(int bVal) { this.bVal = bVal; }
    }

    public ColorSensor(I2cDeviceSynch deviceClient) {
        super(deviceClient, true);
        this.deviceClient.setI2cAddress(ADDRESS_I2C_DEFAULT);
        super.registerArmingStateCallback(false);
        this.deviceClient.engage();
    }

    @Override
    protected synchronized boolean doInitialize() {
        // 1. Kiểm tra ID cảm biến (thường là 0x44 hoặc 0x4D)
        byte id = deviceClient.read8(Register.COMMAND_BIT.bVal | Register.ID.bVal);

        // 2. Thiết lập Integration Time (2.4ms) và Gain (1x)
        writeRegister(Register.ATIME, (byte)0xFF);
        writeRegister(Register.CONTROL, (byte)0x00);

        // 3. Power ON và Enable ADC
        writeRegister(Register.ENABLE, (byte)0x03);
        return true;
    }

    // Hàm ghi vào thanh ghi (cần Command Bit)
    private void writeRegister(Register reg, byte value) {
        deviceClient.write8(Register.COMMAND_BIT.bVal | reg.bVal, value);
    }

    // Đọc giá trị 16-bit (Little Endian)
// Đọc giá trị 16-bit (Little Endian: byte thấp trước, byte cao sau)
    public int read16(Register reg) {
        // Đọc 2 byte từ địa chỉ thanh ghi (đã kèm COMMAND_BIT)
        byte[] data = deviceClient.read(Register.COMMAND_BIT.bVal | reg.bVal, 2);

        // Gộp 2 byte thành 1 số nguyên 16-bit không dấu
        return TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(data));
    }

    public int getRed() { return read16(Register.RDATAL); }
    public int getGreen() { return read16(Register.GDATAL); }
    public int getBlue() { return read16(Register.BDATAL); }
    public int getClear() { return read16(Register.CDATAL); }

    @Override public Manufacturer getManufacturer() { return Manufacturer.Other; }
    @Override public String getDeviceName() { return "TCS34725 Color Sensor"; }
}