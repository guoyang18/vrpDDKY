import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author liShiWei
 * @date 2020-03-12
 */
public class VRP {
    static String filePath = "D:/vrpDDKY/src/main/resources/jieguo_1.txt";
    public static void main(String[] args) {
        Map<Long, List<Point>> points = getVrpInput(filePath);


        points.forEach((key,value)->{

            System.out.println("batch_no:" +key);
//            System.out.println("------");
//            value.forEach(v -> System.out.println(v.getOrderId()+","+v.getSendLongitude()+","));
            value.forEach(v -> System.out.println("送点纬度:"+v.getSendLatitude()+","+"送点经度:"+v.getSendLongitude()+","));
//            System.out.println("------");

            System.out.println("聚类结果：");

            List<Set<Point>> cluster = PointUtils.cluster(value, 1000);

//            System.out.println("------");
            cluster.forEach( set -> {
                for(Point p:set){
                    System.out.print(p.getOrderId()+",");
                }
                System.out.println();
            });
            System.out.println("------");
        });


        //3.使用for循环利用EntrySet进行遍历
//        for(Map.Entry<Long,List<Point>>entry: points.entrySet()){
////            System.out.println("Item:"+entry.getKey()+" Count:"+entry.getValue().size());
//            for(int i=0;i<entry.getValue().size();i++){
//                System.out.println("list:"+entry.getValue().get(i).toString());
////                System.out.println("order_id:"+entry.getValue().get(i).getOrderId());
////                System.out.println("lat:"+entry.getValue().get(i).getSendLatitude());
////                System.out.println("lng:"+entry.getValue().get(i).getSendLongitude());
//            }
//        }
    }




    static Map<Long, List<Point>> getVrpInput(String filePath){
        Map<Long, List<Point>> batchId2Points = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
            String lineTxt = br.readLine();
            while ((lineTxt = br.readLine()) != null) {
                String[] rawData = lineTxt.split("\t");

                Point p = new Point();
                Long batchNo = Long.parseLong(rawData[0]);
                p.setOrderId(Long.parseLong(rawData[2]));
                p.setDispatchUnixTime(Long.parseLong(rawData[3]));
                p.setStationId(Integer.parseInt(rawData[6]));
                p.setSendLatitude(Double.parseDouble(rawData[7]));
                p.setSendLongitude(Double.parseDouble(rawData[8]));
                p.setShippingType(Integer.parseInt(rawData[10]));
                p.setExpectArrivalUnixTime(Long.parseLong(rawData[14]));
                p.setAuditUnixTime(Long.parseLong(rawData[13]));

                //暂时先用默认值
                p.setOrderHang(false);
                p.setOrderHangSendUnixTime(1L);
                p.setFetchLatitude(39.924695);
                p.setFetchLongitude(116.421868);
                p.setEstimateUnixTime(1L);
                p.setSugUnixTime(1L);

                List<Point> points;
                if(batchId2Points.containsKey(batchNo)){
                    points = batchId2Points.get(batchNo);
                }else{
                    points = new ArrayList<>();
                }

                points.add(p);
                batchId2Points.put(batchNo, points);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        return batchId2Points;
    }
}
