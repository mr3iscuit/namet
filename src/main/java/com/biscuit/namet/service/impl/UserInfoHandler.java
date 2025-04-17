package com.biscuit.namet.service.impl;

import com.biscuit.namet.dto.DataRequest;
import com.biscuit.namet.service.IUserINfoHandler;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserInfoHandler implements IUserINfoHandler {
    private final BotService botService;

    @Override
    public void handleData(DataRequest dataRequest) {

        String[] messages = processSensorData(
                dataRequest.getHumidity(),
                dataRequest.getSoil(),
                dataRequest.getPressure(),
                dataRequest.getGasResistance(),
                dataRequest.getAvgHumidity(),
                dataRequest.getAvgTemperature(),
                dataRequest.getTemperature()
        ).toArray(new String[0]);

        TelegramMessage message = TelegramMessage
                .builder()
                .data(dataRequest.toString())
                .messages(List.of(messages))
                .build()
        ;

        botService.sendTelegramNotification(
                message.toString(),
                "1205361240"
        );

        botService.sendTelegramNotification(
                message.toString(),
                "1198999448"
        );
    }

    public List<String> processSensorData(
            double humidity,
            int soilValue,
            double pressure,
            double gasRes,
            double ortaHumidity,
            double ortaTemp,
            double temperature
    ) {
        List<String> messages = new ArrayList<>();

        // === Temperaturun vəziyyətini yoxlayırıq ===
        messages.add("🌡 Temperatur (Orta): " + temperature + " °C");
        if (temperature < 10) {
            messages.add("⚠ Çox soyuq: Toxum çürüyə bilər, cücərməz");
        }
        else if (temperature >= 10 && temperature < 15) {
            messages.add("⚠ Soyuq: Bitkilər yaxşı böyüməz");
        }
        else if (temperature >= 15 && temperature < 25) {
            messages.add("✅ Əla temperatur: Bitkilər mükəmməl böyüyər");
        }
        else if (temperature >= 25 && temperature <= 30) {
            messages.add("⚠ Normal temperatur: Bitkilər böyüyür amma diqqətli ol");
        }
        else if (temperature > 30 && temperature <= 35) {
            messages.add("⚠ Çox isti: Kök sistemi yanar, bitki quruyar");
        }
        else {
            messages.add("❌ Çox isti: Hər şey yandı! Temperatur təhlükəlidi!");
        }

        // === Rütubətin vəziyyəti ===
        String humidityStatus;
        if (humidity < 30) {
            humidityStatus = "Çox quru (0-30%) - Rütubət çox aşağıdır, bu halda bitkilər üçün kifayət qədər su təmin olunmur. Bitkilərin su itirməsi risklidir.";
        }
        else if (humidity >= 30 && humidity < 50) {
            humidityStatus = "Normal quru (30-50%) - Rütubət səviyyəsi orta səviyyədədir. Bitkilər üçün ideal şəraitdə deyil, amma hələ də böyüməyə imkan verir.";
        }
        else if (humidity >= 50 && humidity < 70) {
            humidityStatus = "Normal (50-70%) - Bitkilər üçün əla şərait. Bu rütubət səviyyəsi, əksər bitkilərin yaxşı inkişaf etməsi üçün idealdır.";
        }
        else if (humidity >= 70 && humidity < 90) {
            humidityStatus = "Yüksək rutubət (70-90%) - Çox yüksək rütubət bitkilərin böyüməsini mənfi təsir edə bilər. Bəzi bitkilər rutubətin yüksək olduğu mühitlərdə inkişaf etməyə çətinlik çəkir.";
        }
        else {
            humidityStatus = "Çox yüksək rutubət (90-100%) - Bitkilərin kök sistemi zədələnə bilər. Çox yüksək rütubət, bəzi bitkilərdə çürüməyə səbəb ola bilər.";
        }
        messages.add("🌱 Rütubət: " + humidity + "% - " + humidityStatus);

        // === Torpaq nəmlik səviyyəsi şərh edilir ===
        messages.add("🌱 Torpaq nəmlik (ADC): " + soilValue);
        if (soilValue < 800) {
            messages.add("✅ Torpaq çox yaşdır");
        }
        else if (soilValue < 1400) {
            messages.add("✅ Torpaq yaşdır");
        }
        else if (soilValue < 2000) {
            messages.add("✅ Torpaq normaldır");
        }
        else if (soilValue < 2400) {
            messages.add("✅ Torpaq quru, sulamağa ehtiyac var");
        }
        else {
            messages.add("⚠ [Sensor çıxıb!]"); // ADC çox böyük dəyər verirsə — problem var
        }

        // === Təzyiqə əsasən hava şəraiti proqnozu ===
        messages.add("📈 Təzyiq: " + pressure + " hPa");
        if (pressure < 1000) {
            messages.add("⚠ Aşağı təzyiq — yağış, külək, qasırğa ola bilər");
        }
        else if (pressure >= 1000 && pressure < 1013) {
            messages.add("🌦 Orta təzyiq — hava dəyişkən ola bilər");
        }
        else if (pressure == 1013) {
            messages.add("✅ Normal təzyiq — standart atmosfer təzyiqi");
        }
        else if (pressure > 1013 && pressure <= 1025) {
            messages.add("🌞 Yüksək təzyiq — yaxşı hava");
        }
        else {
            messages.add("⚠ Çox yüksək təzyiq — yağışsız");
        }

        // === Gas Resistance dəyərini əlavə edirik ===
        messages.add("💨 Gas Resistance: " + gasRes + " KOhm");
        if (gasRes > 40) {
            messages.add("✅ Hava çox təmizdir — rural (kənd) səviyyəsi");
        }
        else if (gasRes > 20 && gasRes <= 40) {
            messages.add("✅ Təmiz hava — şəhər ətrafı səviyyə");
        }
        else if (gasRes > 10 && gasRes <= 20) {
            messages.add("⚠ Orta hava keyfiyyəti — şəhər havası, avtomobil qazı ola bilər");
        }
        else if (gasRes > 5 && gasRes <= 10) {
            messages.add("❌ Pis hava — sənaye qazları və VOC çirkliliyi yüksəkdir");
        }
        else if (gasRes <= 5) {
            messages.add("💀 AĞIR ZƏHƏRLİ MÜHİT — qaz sızması, ciddi təhlükə!!!");
        }
        else {
            messages.add("❓ Naməlum qaz müqaviməti dəyəri");
        }

        return messages;
    }

    @Data
    @Builder
    private static class TelegramMessage {
        private List<String> messages;
        private String data;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("📊 Sensor məlumatları:\n");
            sb.append(data).append("\n");
            sb.append("📈 Sensor dəyərləri:\n");

            for (String message : messages) {
                sb.append(message).append("\n");
            }

            return sb.toString();
        }
    }
}
