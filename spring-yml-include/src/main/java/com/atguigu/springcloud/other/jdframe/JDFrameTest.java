package com.atguigu.springcloud.other.jdframe;

import io.github.burukeyou.dataframe.iframe.JDFrame;
import io.github.burukeyou.dataframe.iframe.SDFrame;
import io.github.burukeyou.dataframe.iframe.window.Window;
import io.github.burukeyou.dataframe.iframe.window.round.Range;

import java.util.ArrayList;
import java.util.List;

public class JDFrameTest {

    static List<WebPvDto> dataList = new ArrayList<>();

    static {
        dataList.add(new WebPvDto("a", 0, 1));
        dataList.add(new WebPvDto("a", 1, 5));
        dataList.add(new WebPvDto("a", 2, 7));
        dataList.add(new WebPvDto("a", 3, 3));
        dataList.add(new WebPvDto("a", 4, 2));
        dataList.add(new WebPvDto("a", 5, 4));
        dataList.add(new WebPvDto("a", 6, 4));
        dataList.add(new WebPvDto("b", 7, 1));
        dataList.add(new WebPvDto("b", 8, 4));
        dataList.add(new WebPvDto("b", 7, 6));
        dataList.add(new WebPvDto("b", 8, 2));
    }

    public static void main(String[] args) {
        // 等价于 select ROW_NUMBER() over(partition by type order pv_count desc)
        SDFrame.read(dataList)
                .window(Window.groupBy(WebPvDto::getType).sortDesc(WebPvDto::getPvCount))
                .overRowNumberS(WebPvDto::setValue)
                .show(30);

        // 生成排名号，相同值排名一样，排名不连续 。 如： 1 2 2 2 5 6 7
        // 等价于 select rank() over(partition by type order pv_count desc)
        SDFrame.read(dataList)
                .window(Window.groupBy(WebPvDto::getType).sortDesc(WebPvDto::getPvCount))
                .overRankS(WebPvDto::setValue)
                .show(30);

        // 生成排名号，相同值排名一样，排名连续 如 1 2 2 2 3 4 5
        // 等价于 select  DENSE_RANK() over(partition by type order pv_count desc)
        SDFrame.read(dataList)
                .window(Window.groupBy(WebPvDto::getType).sortDesc(WebPvDto::getPvCount))
                .overDenseRankS(WebPvDto::setValue)
                .show(30);

        // 生成百分比排名号。 使用公式： (rank排名号-1) / (窗口行数-1)
        // 等价于 select  PERCENT_RANK() over(partition by type order pv_count desc)
        SDFrame.read(dataList)
                .defaultScale(6)
                .window(Window.groupBy(WebPvDto::getType).sortDesc(WebPvDto::getPvCount))
                .overPercentRankS(WebPvDto::setValue)
                .show(30);

        // 计算窗口内行数
        //  等价于SQL:  select count(*) over(partition by type order by pv_count desc rows between UNBOUNDED PRECEDING and CURRENT ROW)
        SDFrame.read(dataList)
                .window(Window.groupBy(WebPvDto::getType).sortDesc(WebPvDto::getPvCount).roundStartRow2CurrentRow())
                .overCountS(WebPvDto::setValue)
                .show(30);

        // 计算窗口内的和
        // 等价于 select sum(pv_count) over(rows between 1 PRECEDING and 2 FOLLOWING)
        JDFrame.read(dataList)
                .window(Window.roundBetweenBy(Range.BEFORE(1),Range.AFTER(2)))
                .overSumS(WebPvDto::setValue,WebPvDto::getPvCount)
                .show(30);

        // 计算窗口内的平均值
        // 等价于 select avg(pv_count) over(partition by type )
        SDFrame.read(dataList)
                .defaultScale(4)
                .window(Window.groupBy(WebPvDto::getType))
                .overAvgS(WebPvDto::setValue,WebPvDto::getPvCount)
                .show(30);

        // 计算窗口内的最大值
        // 等价于 select max(pv_count) over(partition by type order pv_count asc)
        SDFrame.read(dataList)
                .window(Window.groupBy(WebPvDto::getType).sortAsc(WebPvDto::getPvCount))
                .overMaxValueS(WebPvDto::setValue,WebPvDto::getPvCount)
                .show(30);

        // 计算窗口内的最小值
        // 等价于 select min(pv_count) over(rows between CURRENT ROW and 2 FOLLOWING)
        SDFrame.read(dataList)
                .window(Window.roundCurrentRow2AfterBy(2))
                .overMinValueS(WebPvDto::setValue,WebPvDto::getPvCount)
                .show(30);

        // 获取当前行的前N行数据
        // 等价于 select lag(pv_count,2) over(partition by type order pv_count desc)
        SDFrame.read(dataList)
                .window(Window.groupBy(WebPvDto::getType).sortDesc(WebPvDto::getPvCount))
                .overLagS(WebPvDto::setValue,WebPvDto::getPvCount,2)
                .show(30);

        // 获取当前行的后N行数据
        // 等价于 select lead(pv_count,3) over()
        SDFrame.read(dataList)
                .window()
                .overLeadS(WebPvDto::setValue,WebPvDto::getPvCount,3)
                .show(30);

        // 获取窗口范围内的第N行数据
        // 等价于 select NTH_VALUE(pv_count,2) over(rows between 1 PRECEDING and CURRENT ROW)
        SDFrame.read(dataList)
                .window(Window.roundBefore2CurrentRowBy(3))
                .overNthValueS(WebPvDto::setValue,WebPvDto::getPvCount,2)
                .show(30);

        // 获取窗口范围内的第1行数据
        // 等价于 select FIRST_VALUE(pv_count) over(rows between 2 PRECEDING and CURRENT ROW)
        SDFrame.read(dataList)
                .window(Window.roundBetweenBy(Range.BEFORE(2), Range.CURRENT_ROW))
                .overFirstValueS(WebPvDto::setValue,WebPvDto::getPvCount)
                .show(30);

        // 获取窗口范围内的最后一行数据
        // 等价于 select LAST_VALUE(pv_count) over(rows between 2 PRECEDING and 2 FOLLOWING)
        SDFrame.read(dataList)
                .window(Window.roundBeforeAfterBy(2,2))
                .overLastValueS(WebPvDto::setValue,WebPvDto::getPvCount)
                .show(30);

        // 给窗口尽量均匀的分成N个桶， 每个桶的编号从1开始， 如果分布不均匀，则优先分配给最小的桶，桶之间的大小差值最多不超过1
        // 等价于 select  Ntile(3) over(partition by type order pv_count desc)
        SDFrame.read(dataList)
                .window(Window.groupBy(WebPvDto::getType))
                .overNtileS(WebPvDto::setValue,3)
                .show(30);

        // 累积分布值， 统计的是 （小于等于当前排名号的行数 / 窗口行数） 的比率
        // select cume_dist() over(partition by type order pv_count desc)
        SDFrame.read(dataList)
                .window(Window.groupBy(WebPvDto::getType).sortDesc(WebPvDto::getPvCount))
                .overCumeDistS(WebPvDto::setValue)
                .show(30);
    }

}
