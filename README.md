# Weather Reporter
weather reporter program using JAVA Swing.

### Function introduction
Show weather information of recent 7 days, What's more, the PLACE could be adjust by IP address automatic, which means that you could use this PROGRAM anywhere.

### Workflow Blurb
 - Get local IP address: visit https://sapi.k780.com/?app=ip.local&format=json, you could get a JSON object which CONTAIN IP address, then using JSON-2.2.4.JAR to parse it and get local IP.
 - Get PINYIN city name: visit http://ip.taobao.com/service/getIpInfo.php?ip=(IP:_you_have_got_last_step) , you could get a CHINESE city name where you are now, then using PINYIN4J.JAR to parse it and get the PINYIN city name.
 - Get weather: visit https://free-api.heweather.com/v5/forecast?city=(PINYIN_city_name:_you_have_got_last_step)&key=(KEY:_you_need_register_first_to_get_one) , you could get a JSON object which CONTAIN weather information,  then using JSON-2.2.4.JAR to parse it and draw them on UI.
 
### Develop Environment
> IntelliJ IDEA 15.0.2

> java version "1.8.0_121"

> Java(TM) SE Runtime Environment (build 1.8.0_121-b13)

> Java HotSpot(TM) 64-Bit Server VM (build 25.121-b13, mixed mode)