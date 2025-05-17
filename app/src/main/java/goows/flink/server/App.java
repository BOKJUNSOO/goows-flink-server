package goows.flink.server;

import goows.flink.server.util.KomoranTokenizer;
import goows.flink.server.util.Top5Sink;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {

        // flink application entry point !_!
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 카프카 토픽으로부터 데이터 구독
        DataStream<String> text = env.fromElements(
            "‘등이 간지러워 긁으려는데 왼손으로도 오른손으로도 닿지 않는 지점이 울진’이라는 우스갯소리가 있다. 국내에서 공기가 가장 맑고 때 묻지 않은 자연을 지니고 있지만 울진은 접근성이 아쉬웠다. 찾기 쉽지 않은 곳이라고 단점만 있는 것은 아니었다. 사람들 등쌀에서 한 발짝 벗어나 있었기에 자연환경이 잘 보존돼 왔다. 그래서 왕피천과 불영계곡 등 맑은 물과 아름다운 금강송이 자라는 원시적인 숲이 울진에 있다. \n" +
                    "\n" +
                    "그런 울진이 올해부터 사람들에게 더 가까워졌다. 기차역이 없던 울진에 상·하행 하루 16번 열차가 다니기 시작했다. 동해선 포항~삼척 구간이 열리면서 울진에만 기차역이 7군데 생겼다. ITX-마음과 누리, 두 종류의 열차가 운행돼 울진 주민들의 숙원이 풀린 셈이다. 교통이 획기적으로 개선되면서 울진을 찾는 관광객들도 크게 늘어날 듯하다. \n" +
                    "\n" +
                    "산·바다·온천욕 즐길 수 있는 백암산\n" +
                    "\n" +
                    "산과 계곡과 바다, 그리고 온천욕을 한 세트로 즐길 수 있는 곳이 울진에 있다. 백암산白岩山(1,004m)은 낙동정맥에서 동해 쪽으로 슬쩍 가지 친 능선에 솟아오른 봉우리다. 아름드리 소나무와 참나무가 우거진 원시적인 숲은 우리 몸 구석구석을 피톤치드 가득한 맑은 공기로 채워 준다. 장대처럼 솟구친 백암산 정상은 동해 바다와 멀리 태백산, 청송 일대 산봉이 한눈에 들어와 꽉 막힌 가슴을 시원하게 열어 준다. 칠보산으로 내닫는 낙동정맥과 경북 영양 일월산(1,218m)을 비롯해 울진은 물론, 영덕, 영양, 안동 일원의 산봉과 동해바다가 파노라마를 이룬다.\n" +
                    "\n" +
                    "신선계곡은 백암산 북동릉과, 낙동정맥, 금장지맥~구주령산 산줄기로 둘러싸인 웅장하고 깊은 계곡이다. 협곡과 깊은 소는 섬뜩할 정도로 맑고 산사면과 산릉에 빼곡한 소나무 숲은 골짜기를 한층 신비롭게 해준다. 사람 때가 덜 묻은 이 계곡에는 수달이 산다. \n" +
                    "\n" +
                    "신선계곡은 평해읍 앞바다로 흘려드는 남대천 최상류 물줄기 중 하나로 전하는 얘기도 많다. 조선 선조 때 영의정 아계鵝溪 이산해李山海(1539~1609)가 평해로 유배 왔을 때 협곡 안에 들어선 뒤 그 감흥을 <아예유고>의 ‘서촌기’에 묘사한 바 있고, 구한말 항일의병장 신돌석(1878~1908) 장군이 왜병에 맞서 치열한 전투를 벌인 뒤 군사를 이끌고 들어와 전열을 가다듬었던 곳이기도 하다.\n" +
                    "\n" +
                    "데크 조성돼 가족 탐방에도 적합\n" +
                    "\n" +
                    "백암산 정상을 오를 수 있는 계곡 등산 코스인데도 ‘신선계곡 생태탐방로’라는 별도의 걷기길을 만들었을 정도로 풍광이 멋지다. 계곡을 타고 쏟아지는 크고 작은 폭포만 200여 개에 이르며 굽이 하나를 돌 때마다 절경의 소沼들이 줄지어 나타났다가 사라진다. \n" +
                    "\n" +
                    "신선계곡의 위험구간은 대부분 데크 길이 조성돼 있어 사뭇 지루할 듯 하나 빼어난 골짜기 풍광에 잡생각이 끼어들 틈이 없다. 신선계곡의 옛 이름은 선시골로 계곡이 웅장한 만큼 지계곡 또한 많다. 닭벼슬골, 가마실골, 용지곡, 뱀골, 연어곡 등 수많은 가닥의 실계곡이 모인 계곡이다.\n" +
                    "\n" +
                    "신선이 목욕하고 놀았다는 신선탕, 다락소多樂沼 일원은 암반이 널찍하게 펼쳐져 누구라도 짐을 내려놓고 쉬고 싶을 만큼 편안하다. 용소는 거대한 용이 몸부림치면서 만든 바위 협곡 같다. 조각가가 다듬어놓은 듯 매끈한데다가 시퍼런 물줄기는 소와 담에 잠겼다 솟구치고 흘러내리면서 신비감을 더한다. 안개 자욱한 날, 신선계곡 용소에 살던 이무기가 승천하는데 어부가 던진 창에 맞아 요동치다가 백암산 팔선대에 폭포를 만들고, 월송정의 용정에 우물을 만든 뒤 근처 바닷가에 용바위로 굳어버렸다는 전설이 전한다. \n" +
                    "\n" +
                    "계곡 속으로 깊이 들어가다 보면 ‘참새 눈물나기’,‘다람쥐 한숨제기’같은 재미있는 이름들이 나온다. 참새 눈물나기는 지세가 가파르고 험준해 날아다니는 참새도 눈물을 흘리며 지나갈 정도로 힘든 곳이라는 뜻이다. 다람쥐 한숨제기는 암석이 수십 층 층계를 이루고 있어 다람쥐도 한달음에 뛰어 오르지 못하고 숨을 돌려야 오를 수 있는 곳이라서 붙여진 이름이다.\n" +
                    "\n" +
                    "백암온천과 후포항의 싱싱한 해산물\n" +
                    "\n" +
                    "신선계곡을 탐방하고 정상에 오른 후 백암온천단지로 내려오는 산행은 약 11km 거리, 7시간 정도 걸린다. 물 좋기로 소문난 백암온천은 산행의 피로를 씻어 주는 특효약이다. 백암온천에서 17km 떨어진 후포항에는 동해의 싱싱한 해산물을 합리적인 가격에 즐길 수 있는 식당들이 있다.\n" +
                    "\n" +
                    "월간산 5월호 기사입니다.\n" +
                    "\n"
        );

        // one-off to sink
        text
                .flatMap(new KomoranTokenizer())
                .keyBy(tuple -> tuple.f0)
                .sum(1)
                .addSink(new Top5Sink());
        env.execute("Top 5 Elements by Integer Value");
    }
}

