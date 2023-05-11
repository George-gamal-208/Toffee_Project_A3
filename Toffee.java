import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;
//import java.io.Vector;
//import java.util.ArrayList;
import java.util.*;
import java.io.*;
class User{
    protected String name;
    protected String password;
    protected String status;
    protected String email;
    protected String address;
    public boolean login(String emal, String password1) {
        boolean valid=false;
        String word="";
        String mail="";
        String pass="";
        try{
            File myfile=new File("customers.txt");
            Scanner myreader=new Scanner (myfile);
            while(myreader.hasNextLine()){
                Vector<String>v=new Vector<String>();
                String data=myreader.nextLine();
                for(int i=0;i<data.length();i++){
                    if(data.charAt(i)!='|'){
                        word+=data.charAt(i);
                    }else{
                        v.add(word);
                        word="";
                    }
                }
                mail=v.get(0);
                pass=v.get(1);
                if(mail.equalsIgnoreCase(emal)&&pass.equalsIgnoreCase(password1)){
                    email=emal;
                    password=password1;
                    valid=true;
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("error");
        }
        return valid;
    }
}
class Customer extends User{
    protected Vector<Customer> customers=new Vector<Customer>();
    protected Vector<Item> items=new Vector<Item>();
    protected Vector<Order> orders=new Vector<Order>();
    protected int loyality_points;
    protected Shopping_Cart cart;
    protected Order order;
    protected Customer c;
    public Customer(){
        //cart.total_price=0;
        cart=new Shopping_Cart();
    }
    public Customer(String email,String password,String name,String address){
        this.email=email;
        this.password=password;
        this.name=name;
        this.address=address;
    }
    public void set(String email,String password,String name,String address){
        this.email=email;
        this.password=password;
        this.name=name;
        this.address=address;
    }
    public void load_orders(){
        String word="";
        try{
            File myfile2=new File("orders.txt");
            Scanner myreader2=new Scanner (myfile2);
            while(myreader2.hasNextLine()){
                Vector<String>v=new Vector<String>();
                String data=myreader2.nextLine();
                for(int i=0;i<data.length();i++){
                    if(data.charAt(i)!='|'){
                        word+=data.charAt(i);
                    }else{
                        v.add(word);
                        word="";
                    }
                }
                Vector<Item>I=new Vector<Item>();
                for(int i=5;i<(v.size()-1);i++){
                    for(Item it:items){
                        if(it.getName().equalsIgnoreCase(v.get(i))){
                            I.add(it);
                            break;
                        }
                    }
                }
                Order o=new Order(v.get(0),v.get(1),v.get(2),v.get(3),v.get(4),I,v.get(v.size()-1));       
                orders.add(o);
            }
        }catch(Exception e){
            System.out.println("error");
        }
    }
    public void load_items(){
        String word="";
        String id="",Price="",Discount="",Quantity="";
        int id1=0,price1=0,discount1=0,quantity1=0;
        try{
            File myfile1=new File("items.txt");
            Scanner myreader1=new Scanner (myfile1);
            while(myreader1.hasNextLine()){
                Vector<String>v=new Vector<String>();
                String data=myreader1.nextLine();
                for(int i=0;i<data.length();i++){
                    if(data.charAt(i)!='|'){
                        word+=data.charAt(i);
                    }else{
                        v.add(word);
                        word="";
                    }
                }
                id=v.get(0);
                Price=v.get(6);
                Discount=v.get(8);
                Quantity=v.get(10);
                try{
                    id1=Integer.parseInt(id);
                    price1=Integer.parseInt(Price);
                    discount1=Integer.parseInt(Discount);
                    quantity1=Integer.parseInt(Quantity);
                }catch(NumberFormatException ex){
                    ex.printStackTrace();
                }
                Item it=new Item(id1,v.get(1),v.get(2),v.get(3),v.get(4),v.get(5),price1,v.get(7),discount1,v.get(9),quantity1);
                items.add(it);
            }
        }catch(Exception e){
            System.out.println("error");
        }
        for(Item i:items){
            System.out.println(i.getID()+" "+i.getName()+" "+i.getCategory()+" "+i.getImageURL()+" "+i.getDescription()+" "+i.getBand()+" "+i.getPrice()+" "+i.getQuantity()+" "+i.getDiscount());
        }
    }
    
    public void load(){
        String word="";
        try{
            File myfile=new File("customers.txt");
            Scanner myreader=new Scanner (myfile);
            while(myreader.hasNextLine()){
                Vector<String>v=new Vector<String>();
                String data=myreader.nextLine();
                for(int i=0;i<data.length();i++){
                    if(data.charAt(i)!='|'){
                        word+=data.charAt(i);
                    }else{
                        v.add(word);
                        word="";
                    }
                }
                Customer c=new Customer(v.get(0),v.get(1),v.get(2),v.get(3));
                customers.add(c);
            }
        }catch(Exception e){
            System.out.println("error");
        }
    }
    public void save(){
        try{
            FileWriter fl=new FileWriter("customers.txt");
            for(Customer c:customers){
                fl.write(c.email+"|"+c.password+"|"+c.name+"|"+c.address+"|");
                fl.write("\n");
            }
            fl.close();
        } catch(Exception e){
            System.out.println("error");
        }
    }
    public void register(String emal,String pass,String nm,String addrss){
        load();
        Scanner in=new Scanner(System.in);
        boolean valid=true;
        while(true){
            valid=true;
            for(Customer customer:customers){
                if(customer.password.equalsIgnoreCase(pass)){
                    valid=false;
                    System.out.println("please enter another password");
                    pass=in.next();
                    break;
                }
            }
            if(valid){
                password=pass;
                Customer c=new Customer(emal,pass,nm,addrss);
                customers.add(c);
                save();
                break;
            }
        }
}
public void additemtocart(Item item){
    cart=new Shopping_Cart();
    int quantity=0;
    cart.set_total_price();
    System.out.println("enter the quantity you want");
    Scanner in=new Scanner(System.in);
    quantity=in.nextInt();
    cart.additem(item, quantity);
}
public Shopping_Cart viewcart(){
    for(int i=0;i<cart.items.size();i++){
        System.out.println(cart.items.get(i).getName());
    }
    return cart;
}
public void searchitem(String item_name){
    //Item it=new Item();
    for(Item i:items){
        if(i.getName().equalsIgnoreCase(item_name)){
            System.out.println(i.getID()+" "+i.getName()+" "+i.getCategory()+" "+i.getImageURL()+" "+i.getDescription()+" "+i.getBand()+" "+i.getPrice()+" "+i.getQuantity()+" "+i.getDiscount());
            //it=i;
            break;
        }
    }
    //return it;
}
public void vieworderhistory(){
    //load_orders();
    for(Order o:orders){
        if(o.customer_pass.equalsIgnoreCase(password)){
            System.out.println("your order");
    System.out.println("Item name | price");
    System.out.println("-------------------------------------------------------");
    for(Item itm:o.items){
        System.out.println(itm.getName()+" "+itm.getPrice());
    }
    System.out.println("Total price"+"  "+o.totalPrice);
    System.out.println("Shipping address"+" "+o.shippingAddress);
    System.out.println("phone number"+" "+o.phoneNumber);
    System.out.println("Date"+" "+o.date);        
        }
    }
}
public void buy(String addrss,String number){
    String s="";
    s=Integer.toString(cart.get_total_price());
    //load_orders();
    order=new Order();
    order.orderNumber="1";
    order.items=cart.items;
    order.totalPrice=s;
    order.customer_pass=password;   
    order.shippingAddress=addrss;
    order.phoneNumber=number;
    order.date="10/5/2023";
    orders.add(order);
    System.out.println("your order");
    System.out.println("Item name | price");
    System.out.println("-------------------------------------------------------");
    for(Item itm:order.items){
        System.out.println(itm.getName()+" "+itm.getPrice());
    }
    System.out.println("Total price"+"  "+order.totalPrice);
    System.out.println("Shipping address"+" "+order.shippingAddress);
    System.out.println("phone number"+" "+order.phoneNumber);
    System.out.println("Date"+" "+order.date);
    System.out.println("An otp message will sent to your mobile to confirm your order");
    try{
        FileWriter fl1=new FileWriter("orders.txt");
        for(Order o:orders){
            fl1.write(o.orderNumber+"|"+o.customer_pass+"|"+o.shippingAddress+"|"+o.phoneNumber+"|"+o.date+"|");
            for(Item i:o.items){
                fl1.write(i.getName()+"|");
            }
            fl1.write(o.totalPrice+"|");
            fl1.write("\n");
        }
        fl1.close();
    } catch(Exception e){
        System.out.println("error");
    }
}
}

class Shopping_Cart{
    protected Vector<Item>items=new Vector<Item>();
    protected int total_price;
    public Shopping_Cart(){

    }
    protected void set_total_price(){
        total_price=0;
    }
    public void additem(Item item,int quantity){
        for(int i=0;i<quantity;i++){
            items.add(item);
            total_price+=item.getPrice();
        }
    }
    public void removeitem(Item item){
        if(items.isEmpty()){
            System.out.println("There is no items");
        }else{
            items.remove(item);
            total_price-=item.getPrice();
        }
    }
    public int get_total_price(){
        return total_price;
    }
    public void view_cart(){
        for(int i=0;i<items.size();i++){
            System.out.println(items.get(i).getName());
        }
    }
    public void make_cart_empty(){
        items.clear();
        total_price=0;
    }
}
class Item {
    private int ID;
    private String name;
    private String category;
    private String imageURL;
    private String description;
    private String band;
    private int price;
    private String status;
    private int discount;
    private String unitType;
    private int quantity;
    public Item(){
        
    }
    public Item(int ID, String name, String category, String imageURL, String description,
                String band, int price, String status, int discount, String unitType,
                int quantity) {
        this.ID = ID;
        this.name = name;
        this.category = category;
        this.imageURL = imageURL;
        this.description = description;
        this.band = band;
        this.price = price;
        this.status = status;
        this.discount = discount;
        this.unitType = unitType;
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getDescription() {
        return description;
    }

    public String getBand() {
        return band;
    }

    public int getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public int getDiscount() {
        return discount;
    }

    public String getUnitType() {
        return unitType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
class Order {
    protected String customer_pass;
    protected String orderNumber;
    protected String shippingAddress;
    protected Vector<Item>items=new Vector<Item>();
    protected String totalPrice;
    protected String date;
    protected String phoneNumber;
    protected String giftVoucher;
    protected String otp;
    public Order(){

    }
    public Order(String orderNumber,String pass, String shippingAddress,String phoneNumber, String date,Vector<Item>it, String price) {
        this.orderNumber = orderNumber;
        this.shippingAddress = shippingAddress;
        this.date = date;
        this.phoneNumber = phoneNumber;
        customer_pass=pass;
        totalPrice=price;
        items=it;
    }

    public void addVoucher(String voucher) {
        giftVoucher = voucher;
        //this.totalPrice -= voucher.getAmount(); 
    }

    //public void addItem(Item item, int quantity) {
      //  for (int i = 0; i < quantity; i++) {
        //    this.items.add(item);
          //  this.totalPrice += item.getPrice();
        //}
    //}

    //public void removeItem(Item item) {
      //  if (this.items.remove(item)) {
        //    this.totalPrice -= item.getPrice();
        //}
    //}

    public void changeAddress(String address) {
        this.shippingAddress = address;
    }

    public void makeOTPmssg() {
        this.otp = "1234"; 
    }

    //public boolean isOTPCorrect(String inputOTP) {
      //  return inputOTP.equals(this.otp);
    //}

    public String getTotalPrice() {
        return totalPrice;
    }

    //public void changeQuantity(Item item, int newQuantity) 
    //for (Item i : this.items) {
      //  if (i.equals(item)) {
        //    i.setQuantity(newQuantity);
          //  break;
        //}
    //}
    //recalculateTotalPrice(); // function to calculate totalPrice need to be added

}
class Checkout {
    private Payment paymentMethod;
    private Shopping_Cart cart;
    private String date;
    private Order orderDetails;

    public Checkout(Payment paymentMethod, Shopping_Cart cart, String date, Order orderDetails) {
        this.paymentMethod = paymentMethod;
        this.cart = cart;
        this.date = date;
        this.orderDetails = orderDetails;
    }
    public void choosePaymentMethod(Payment method) {
        this.paymentMethod = method;
    }
}
class Payment {
	private String paymentMethod;
	private static int id=0;
	Payment(String payment)
	{
		paymentMethod = payment;
		id++;
	}
	public void changePaymentMethod(String method)
	{
		paymentMethod = method;
	}
}

public class Toffee{
    public static void main(String[] args){
        Customer C=new Customer();
        int choice=0,choice1=0,choice2=0;
        System.out.println("Welcome to our toffee website");
        System.out.println("1-Register");
        System.out.println("2-login");
        System.out.println("3-view catalog");
        System.out.println("4-exit");
        System.out.println("Enter the choice you want");
        Scanner in=new Scanner(System.in);
        while(choice!=4){
            choice=in.nextInt();
            if(choice==1){
                String name="",email="",password="",address="";
                System.out.println("enter your email");
                email=in.next();
                System.out.println("enter your password");
                password=in.next();
                System.out.println("enter your name");
                name=in.next();
                System.out.println("enter your address");
                address=in.next();
                C.register(email, password, name, address);
                C.load_items();
                C.load_orders();
                System.out.println("This is our catalog of items");
                System.out.println("1-add item to cart");
                System.out.println("2-view cart");
                System.out.println("3-rmove item");
                System.out.println("4-get total price");
                System.out.println("5-make cart empty");
                System.out.println("6-reorder item");
                System.out.println("7-searh item");
                System.out.println("8-redeem voucher");
                System.out.println("9-view order history");
                System.out.println("10-go to checkout");
                System.out.println("11-exit");
                System.out.println("Enter the choice you want");
                while(choice1!=11){
                    choice1=in.nextInt();
                    if(choice1==1){
                        int item_N=0,q=0;
                        System.out.println("enter the item number you want");
                        item_N=in.nextInt();
                        System.out.println("enter the quantity you want");
                        q=in.nextInt();
                        C.cart.additem(C.items.get(item_N-1),q);
                        System.out.println("Item added");
                    }else if(choice1==2){
                        C.cart.view_cart();
                    }else if(choice1==3){
                        int item_N=0;
                        System.out.println("enter the item number you want to remove");
                        item_N=in.nextInt();
                        C.cart.removeitem(C.items.get(item_N-1));
                    }else if(choice1==4){
                        int x=C.cart.get_total_price();
                        System.out.println(x);
                    }else if(choice1==5){
                        C.cart.make_cart_empty();
                    }else if(choice1==7){
                        String s="";
                        s=in.next();
                        C.searchitem(s);
                    }else if(choice1==10){
                        String cust_addrs="",cust_num="";
                        System.out.println("enter your address");
                        cust_addrs=in.next();
                        System.out.println("enter your number");
                        cust_num=in.next();
                        C.buy(cust_addrs, cust_num);
                    }else if(choice1==9){
                        C.vieworderhistory();
                    }
                }
            }else if(choice==2){
                String email="",password="";
                System.out.println("enter your email");
                email=in.next();
                System.out.println("enter your password");
                password=in.next();
                Boolean val=false;
                while(true){
                    val=C.login(email, password);
                    if(val){
                        break;
                    }else{
                        System.out.println("enter your email");
                        email=in.next();
                        System.out.println("enter your password");
                        password=in.next();
                    }
                }
                C.load_items();
                C.load_orders();
                System.out.println("This is our catalog of items");
                System.out.println("1-add item to cart");
                System.out.println("2-view cart");
                System.out.println("3-rmove item");
                System.out.println("4-get total price");
                System.out.println("5-make cart empty");
                System.out.println("6-reorder item");
                System.out.println("7-searh item");
                System.out.println("8-redeem voucher");
                System.out.println("9-view order history");
                System.out.println("10-go to checkout");
                System.out.println("11-exit");
                System.out.println("Enter the choice you want");
                while(choice1!=11){
                    choice1=in.nextInt();
                    if(choice1==1){
                        int item_N=0,q=0;
                        System.out.println("enter the item number you want");
                        item_N=in.nextInt();
                        System.out.println("enter the quantity you want");
                        q=in.nextInt();
                        C.cart.additem(C.items.get(item_N-1),q);
                        System.out.println("Item added");
                    }else if(choice1==2){
                        C.cart.view_cart();
                    }else if(choice1==3){
                        int item_N=0;
                        System.out.println("enter the item number you want to remove");
                        item_N=in.nextInt();
                        C.cart.removeitem(C.items.get(item_N-1));
                    }else if(choice1==4){
                        int x=C.cart.get_total_price();
                        System.out.println(x);
                    }else if(choice1==5){
                        C.cart.make_cart_empty();
                    }else if(choice1==7){
                        String s="";
                        s=in.next();
                        C.searchitem(s);
                    }else if(choice1==10){
                        String cust_addrs="",cust_num="";
                        System.out.println("enter your address");
                        cust_addrs=in.next();
                        System.out.println("enter your number");
                        cust_num=in.next();
                        C.buy(cust_addrs, cust_num);
                    }else if(choice1==9){
                        C.vieworderhistory();
                    }
                }
            }else if(choice==3){
                C.load_items();
            }
        }
    }
}