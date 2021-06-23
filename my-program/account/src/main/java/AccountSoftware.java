

import java.util.Scanner;

public class AccountSoftware {

    /**
     * 简易记账软件
     */
    public static void main(String[] args) {
        System.out.println("--------欢迎使用鲨鱼记账系统---------");
        System.out.println("1.收支明细");
        System.out.println("2.登记支出");
        System.out.println("3.登记收入");
        System.out.println("4.退出");
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入你要选择的功能：");
        int choice = scanner.nextInt();
        while (choice > 4){
            System.out.println("输入错误，请重新选择！");
            int newChoice = scanner.nextInt();
            choice = newChoice;
        }
        switch (choice){
            case 1:
                System.out.println("收支明细：");
                break;
            case 2:
                System.out.println("登记支出：");
                break;
            case 3:
                System.out.println("登记收入：");
                break;
            case 4:
                System.out.println("是否退出：");
                return;

        }















        System.out.println("~~~~~小鲨鱼记账系统~~~~~");
    }
}
