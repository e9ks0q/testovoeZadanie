import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

class Calc {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("Вводите пример: ");
            String line = scanner.nextLine();

            try {
                String[] symbols = line.split(" ");

                Number firstNumber = NumberService.parseAndValidate(symbols[0]);
                Number secondNumber = NumberService.parseAndValidate(symbols[2], firstNumber.getType());
                String result = ActionService.calculate(firstNumber, secondNumber, symbols[1]);
                System.out.println(result);

            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                break;
            }
        }

    }

}

class ActionService {

    public static String calculate(Number first, Number second, String action) throws Exception {

        int result;

        switch (action) {
            case "+":
                result = first.vychisleniya() + second.vychisleniya();
                break;
            case "-":
                result = first.vychisleniya() - second.vychisleniya();
                break;
            case "*":
                result = first.vychisleniya() * second.vychisleniya();
                break;
            case "/":
                result = first.vychisleniya() / second.vychisleniya();
                break;
            default:
                throw new Exception("При вводе пользователем строки, не соответствующей одной из вышеописанных арифметических операций, приложение выбрасывает исключение и завершает свою работу.");
        }

        if (first.getType() == NumberType.RIMSKIE) {
            return NumberService.toRimskieNumber(result);
        } else return String.valueOf(result);
    }
}

class NumberService {

    private final static TreeMap < Integer, String > rimskieString = new TreeMap<>();

    static {
        rimskieString.put(1, "I");
        rimskieString.put(4, "IV");
        rimskieString.put(5, "V");
        rimskieString.put(9, "IX");
        rimskieString.put(10, "X");
        rimskieString.put(40, "XL");
        rimskieString.put(50, "L");
        rimskieString.put(90, "XC");
        rimskieString.put(100, "C");
    }

    static Number parseAndValidate(String symbol) throws Exception {

        int value;
        NumberType type;

        try {
            value = Integer.parseInt(symbol);
            type = NumberType.ARABSKIE;
        }catch (NumberFormatException e) {
            value = toArabskieNumber(symbol);
            type = NumberType.RIMSKIE;
        }

        if (value < 1 || value > 10) {
            throw new Exception("Для ввода доступны числа от 1 до 10 / от I до X");
        }

        return new Number(value, type);
    }

    static Number parseAndValidate(String symbol, NumberType type) throws Exception {

        Number number = parseAndValidate(symbol);
        if (number.getType() != type) {
            throw new Exception("Калькулятор умеет работать только с арабскими или римскими цифрами одновременно, при вводе пользователем строки вроде 3 + II калькулятор должен выбросить исключение и прекратить свою работу.");
        }

        return number;
    }

    static int letterToNumber(char letter) {

        int result = -1;

        for (Map.Entry < Integer, String > entry: rimskieString.entrySet()) {
            if (entry.getValue().equals(String.valueOf(letter))) result = entry.getKey();
        }
        return result;
    }

    static String toRimskieNumber(int number) {

        int i = rimskieString.floorKey(number);

        if (number == i) {
            return rimskieString.get(number);
        }
        return rimskieString.get(i) + toRimskieNumber(number - i);
    }

    static int toArabskieNumber(String rimskie) throws Exception {
        int result = 0;

        int i = 0;
        while (i < rimskie.length()) {
            char letter = rimskie.charAt(i);
            int num = letterToNumber(letter);

            if (num < 0) throw new Exception("Результатом работы калькулятора с арабскими числами могут быть отрицательные числа и ноль. Результатом работы калькулятора с римскими числами могут быть только положительные числа, если результат работы меньше единицы, выбрасывается исключение");

            i++;
            if (i == rimskie.length()) {
                result += num;
            }else {
                int nextNum = letterToNumber(rimskie.charAt(i));
                if(nextNum > num) {
                    result += (nextNum - num);
                    i++;
                }
                else result += num;
            }
        }
        return result;
    }
}

class Number {

    private int value;
    private NumberType type;

    Number(int value, NumberType type) {
        this.value = value;
        this.type = type;
    }

    int vychisleniya() {
        return value;
    }

    NumberType getType() {
        return type;
    }
}

enum NumberType {
    ARABSKIE,
    RIMSKIE
}