package example.thread;

public class MyThread {
    public static void main(String[] args) {

        Market market = new Market();

        Producer producer = new Producer(market);
        Consumer consumer = new Consumer(market);

        Thread thread1 = new Thread(producer);
        Thread thread2 = new Thread(consumer);
        thread1.start();
        thread2.start();
    }
}

class Market {
    private int GoodsCount = 0;
    private Object lock = new Object();

    public void getBread() {

        synchronized (lock) {

            while (GoodsCount < 1) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            GoodsCount--;
            System.out.println("Потребитель купил 1 товар");
            System.out.println("Количество товара в магазине = " + GoodsCount);
            lock.notify();
        }
    }

    public synchronized void putBread() {

        synchronized (lock) {

            while (GoodsCount >= 5) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            GoodsCount++;
            System.out.println("Производитель добавил на витрину 1 товар");
            System.out.println("Количество товара в магазине = " + GoodsCount);
            lock.notify();
        }
    }
}


class Producer implements Runnable {

    Market market;

    public Producer(Market market) {
        this.market = market;
    }

    @Override
    public void run() {

        for (int i = 0; i < 10; i++) {
            market.putBread();

        }
    }
}

class Consumer implements Runnable {

    Market market;

    public Consumer(Market market) {
        this.market = market;
    }

    @Override
    public void run() {

        for (int i = 0; i < 10; i++) {
            market.getBread();

        }
    }
}