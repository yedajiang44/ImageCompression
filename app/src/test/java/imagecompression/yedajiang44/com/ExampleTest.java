package imagecompression.yedajiang44.com;

import java.util.Scanner;

/**
 * 作者：yedajiang44
 * 时间 2018-10-25 21:08
 * 邮箱：602830483@qq.com
 * 说明:
 */
class ExampleTest {
    enum Fruit {
        apple(1, "苹果", 3.00), pear(2, "梨", 2.50), orange(3, "桔子", 4.10), grape(4, "葡萄", 10.20), exit(0, "退出", 0);
        private double price;
        private String name;
        private int order;

        Fruit(int order, String name, double price) {
            this.name = name;
            this.price = price;
            this.order = order;
        }

        public double getPrice() {
            return price;
        }

        public int getOrder() {
            return order;
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] strings) {
        Scanner input = new Scanner(System.in);
        String inputLineValue = "";
        Fruit fruit = null;
        do {
            fruit = Fruit.apple;
        } while (fruit.getOrder() != 0);

        input.close(); // 关闭资源

//        List<Observable<Integer>> observables = new ArrayList<>();
//        observables.add(Observable.just(1));
//        observables.add(Observable.just(2));
//        observables.add(Observable.just(3));
//        Observable.combineLatest(observables, ss -> {
//            List<Integer> items = new ArrayList<>();
//            for (Object o : ss) {
//                items.add((int) o);
//            }
//            return items;
//        }).subscribe(new Observer<List<Integer>>() {
//            @Override
//            public void onNext(List<Integer> list) {
//                System.out.println(list);
//            }
//        });
    }
}
