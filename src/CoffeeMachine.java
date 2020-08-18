import java.util.Scanner;

public class CoffeeMachine {
    private static final Scanner CONSOLE = new Scanner(System.in);
    private int water;
    private int milk;
    private int beans;
    private int cups;
    private int money;
    private boolean isOn;
    private State state;
    private FillRes fillRes;

    private enum State {
        CHOOSING_ACTION,
        BUYING,
        FILLING
    }

    private enum FillRes {
        WATER("Write how many ml of water do you want to add:"),
        MILK("Write how many ml of milk do you want to add:"),
        BEANS("Write how many grams of coffee beans do you want to add:"),
        CUPS("Write how many disposable cups of coffee do you want to add:");

        String sign;

        FillRes(String sign) {
            this.sign = sign;
        }

        private String getSign() {
            return this.sign;
        }
    }

    private enum Action {
        BUY,
        FILL,
        TAKE,
        REMAINING,
        EXIT
    }
    private enum Coffee {
        ESPRESSO("1", 250, 0, 16, 4),
        LATTE("2", 350, 75, 20, 7),
        CAPUCCINO("3", 200,100, 12, 6);

        String num;
        int waterNeed, milkNeed, beansNeed, price;

        Coffee(String num, int waterNeed,
               int milkNeed, int beansNeed,
               int price) {
            this.num = num;
            this.waterNeed = waterNeed;
            this.milkNeed = milkNeed;
            this.beansNeed = beansNeed;
            this.price = price;
        }

        private static Coffee getByNum(String num) {
            for (Coffee value: values()) {
                if (num.equals(value.num)) {
                    return value;
                }
            }
            return null;
        }


    }

    private CoffeeMachine(int water,
                          int milk,
                          int beans,
                          int cups,
                          int money) {
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.cups = cups;
        this.money = money;
        this.isOn = true;
        this.state = State.CHOOSING_ACTION;
        this.fillRes = FillRes.WATER;
    }

    private void process(final String INPUT) {
        switch (this.state) {
            case CHOOSING_ACTION:
                final Action ACTION = Action.valueOf(INPUT.toUpperCase());
                switch (ACTION) {
                    case BUY:
                        this.state = State.BUYING;
                        break;
                    case FILL:
                        this.state = State.FILLING;
                        System.out.println(this.fillRes.getSign());
                        break;
                    case TAKE:
                        System.out.println("I gave you $" + this.money);
                        this.money = 0;
                        this.state = State.CHOOSING_ACTION;
                        break;
                    case REMAINING:
                        printRemaining();
                        this.state = State.CHOOSING_ACTION;
                        break;
                    case EXIT:
                        this.isOn = false;
                        break;
                }
                break;

            case BUYING:
                if (!INPUT.equals("back")) {
                    final Coffee COFFEE = Coffee.getByNum(INPUT);
                    make(COFFEE);
                }
                this.state = State.CHOOSING_ACTION;
                break;

            case FILLING:
                switch (this.fillRes) {
                    case WATER:
                        this.water += Integer.parseInt(INPUT);
                        this.fillRes = FillRes.MILK;
                        System.out.println(this.fillRes.getSign());
                        break;
                    case MILK:
                        this.milk += Integer.parseInt(INPUT);
                        this.fillRes = FillRes.BEANS;
                        System.out.println(this.fillRes.getSign());
                        break;
                    case BEANS:
                        this.beans += Integer.parseInt(INPUT);
                        this.fillRes = FillRes.CUPS;
                        System.out.println(this.fillRes.getSign());
                        break;
                    case CUPS:
                        this.cups += Integer.parseInt(INPUT);
                        this.fillRes = FillRes.WATER;
                        this.state = State.CHOOSING_ACTION;
                        break;
                }
                break;
        }
    }

    private void make(final Coffee COFFEE) {
        final int CUP = 1;
        if (this.water < COFFEE.waterNeed){
            System.out.println("Sorry, not enough water!");
        }
        else if (this.milk < COFFEE.milkNeed){
            System.out.println("Sorry, not enough milk!");
        }
        else if (this.beans < COFFEE.beansNeed){
            System.out.println("Sorry, not enough beans!");
        }
        else if (this.cups < CUP){
            System.out.println("Sorry, not enough cups!");
        }
        else {
            System.out.println("I have enough resources," +
                    " making you a COFFEE!");
            this.water -= COFFEE.waterNeed;
            this.milk -= COFFEE.milkNeed;
            this.beans -= COFFEE.beansNeed;
            this.cups -= CUP;
            this.money += COFFEE.price;
        }
    }

    private void printRemaining() {
        System.out.println("The coffee machine has:");
        System.out.println(this.water + " of water");
        System.out.println(this.milk + " of milk");
        System.out.println(this.beans + " of coffee beans");
        System.out.println(this.cups + " of disposable cups");
        System.out.println(this.money + " of money");
    }

    public static void main(String[] args) {
        final int WATER = 400;
        final int MILK = 540;
        final int BEANS = 120;
        final int CUPS = 9;
        final int MONEY = 550;
        final CoffeeMachine MACHINE = new CoffeeMachine(WATER, MILK, BEANS, CUPS, MONEY);
        while (MACHINE.isOn) {
            final String INPUT = CONSOLE.nextLine();
            MACHINE.process(INPUT);
        }
    }
}