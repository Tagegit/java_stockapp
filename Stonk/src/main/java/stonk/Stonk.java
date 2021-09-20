package stonk;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Stonk {

    String ticker;
    double price;
    String name;

    //Henter aksjeprisen til "ticker" som bruker søker på
    //Hvis "ticker" ikke er nøyaktig, får en en NullPointerException siden elementet i HTMLen = NULL
    //en blir en viderført til en liste med lignende aksjer, der velger en bare den første og satser på at det er riktig
    public double getStockInfo(String ticker) throws IOException{
        this.ticker = ticker;
        String link = "https://www.marketwatch.com/investing/stock/" + ticker;
        Document doc = Jsoup.connect(link)
                .cookie("AMCVS_CB68E4BA55144CAA0A4C98A5%40AdobeOrg","1")
                .get();
        try {
            this.name = (doc.select("h1.company__name").first().text());
            return this.price = Double.parseDouble(doc.select("bg-quote.value").first().text());
        }
        catch (NullPointerException e){
            ticker = doc.select(".results table tbody tr td a").first().text();
            return getStockInfo(ticker);
        }
    }

    public void buyStonk() {
        try {
            getTickerPrice(ticker);
        } catch (IOException e) {
            
        }
        
    }


	@Override
	public String toString() {
        return "your stock " + name + " har a price of " + price;
    }


    public static void main(String[] args) throws IOException {
        Stonk s = new Stonk();
        System.out.println(s);
        System.out.println(s.getStockInfo("apple"));
        System.out.println(s.name);
    }
}

