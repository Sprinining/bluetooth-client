package com.example.uhf_bluetoothclient.entity;

import com.example.uhf_bluetoothclient.util.DateUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TagCells {
    // 已经被扫描到的标签epc值
    // epc 和 在tagCells中的下标
    private final Map<String, Integer> epcMap = new HashMap<>();
    private final List<TagCell> tagCells = new ArrayList<>();

    public synchronized void addTagCell(LogBaseEpcInfo tagInfo) {
        if (!epcMap.containsKey(tagInfo.getEpc())) {
            // 第一次搜到这张卡
            TagCell tagCell = new TagCell(tagInfo.getEpc());
            tagCell.add(tagInfo.getAntId(), DateUtils.Date2LocalDateTime(tagInfo.getReadTime()));

            // 初始化这个tagCell
            tagCells.add(tagCell);
            epcMap.put(tagInfo.getEpc(), tagCells.indexOf(tagCell));
        } else {
            Integer integer = epcMap.get(tagInfo.getEpc());
            if (integer != null) {
                // 找到epc对应的tagCell
                TagCell tagCell = tagCells.get(integer);
                // 增加天线cell或者只是增加一个count
                tagCell.add(tagInfo.getAntId(), DateUtils.Date2LocalDateTime(tagInfo.getReadTime()));
            }
        }
    }

    @Override
    public String toString() {
        return "TagCells{" +
                "epcMap=" + epcMap +
                ", tagCells=" + tagCells +
                '}';
    }

    public Map<String, Integer> getEpcMap() {
        return epcMap;
    }

    public List<TagCell> getTagCells() {
        return tagCells;
    }

    public void clear() {
        epcMap.clear();
        tagCells.clear();
    }

    public static class TagCell {
        private String epc;
        private List<AntennaCell> antennaCells = new ArrayList<>();

        // 已加入的天线
        private final Map<Integer, Integer> antennaMap = new HashMap<>();

        public synchronized void add(int antennaId, LocalDateTime localDateTime) {
            if (!antennaMap.containsKey(antennaId)) {
                // 没有就先初始化
                AntennaCell antennaCell = new AntennaCell(antennaId, localDateTime, 1);
                antennaCells.add(antennaCell);
                // 加入已经有的天线列表中
                antennaMap.put(antennaId, antennaCells.indexOf(antennaCell));
            } else {
                // 这个天线已经有了，就只要增加一下count
                Integer integer = antennaMap.get(antennaId);
                if (integer != null) {
                    antennaCells.get(integer).addCount();
                }
            }
        }

        public String getEpc() {
            return epc;
        }

        public void setEpc(String epc) {
            this.epc = epc;
        }

        public List<AntennaCell> getAntennaCells() {
            return antennaCells;
        }

        public void setAntennaCells(List<AntennaCell> antennaCells) {
            this.antennaCells = antennaCells;
        }

        public TagCell(String epc) {
            this.epc = epc;
        }

        public TagCell() {
        }

        @Override
        public String toString() {
            return "TagCell{" +
                    "epc='" + epc + '\'' +
                    ", antennaCells=" + antennaCells +
                    ", antennaMap=" + antennaMap +
                    '}';
        }

        public static class AntennaCell {
            // 天线id
            private int antennaId;
            // 首次读取时间
            private LocalDateTime readTime;
            // 该标签在该天线下的读取次数
            private AtomicInteger count = new AtomicInteger(0);

            public int getAntennaId() {
                return antennaId;
            }

            public void setAntennaId(int antennaId) {
                this.antennaId = antennaId;
            }

            public LocalDateTime getReadTime() {
                return readTime;
            }

            public void setReadTime(LocalDateTime readTime) {
                this.readTime = readTime;
            }

            public int getCount() {
                return count.get();
            }

            public void setCount(int count) {
                this.count.set(count);
            }

            public AntennaCell(int antennaId, LocalDateTime readTime, int count) {
                this.antennaId = antennaId;
                this.readTime = readTime;
                this.count.set(count);
            }

            public AntennaCell() {
            }

            public void addCount() {
                count.incrementAndGet();
            }

            @Override
            public String toString() {
                return "AntennaCell{" +
                        "antennaId=" + antennaId +
                        ", readTime=" + readTime +
                        ", count=" + count +
                        '}';
            }
        }
    }
}
