package com.testtask.currencyconverter.model;

import android.os.AsyncTask;
import android.util.Log;

import com.testtask.currencyconverter.common.Currency;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class CurrencyModel {
    private final List<Currency> data = new ArrayList<>();

    public void loadCurrencies(LoadCurrenciesCallback callback) {
        LoadCurrencyTask task = new LoadCurrencyTask(callback);
        task.execute();
    }

    public List<Currency> getData() {
        return data;
    }

    public List<String> getShownData() {
        List<String> shownData = new ArrayList<>();
        for (Currency currency : data) {
            shownData.add(String.format("%s %s", currency.getName(), currency.getCharCode()));
        }

        return shownData;
    }

    public class LoadCurrencyTask extends AsyncTask<Void, Void, List<Currency>> {
        private final LoadCurrenciesCallback callback;

        public LoadCurrencyTask(LoadCurrenciesCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<Currency> doInBackground(Void... voids) {
            List<Currency> currencies = new ArrayList<>();
            currencies.add(new Currency(0, "RUB", 1, "Российский рубль", 1f));

            Document doc = getXmlDoc("http://www.cbr.ru/scripts/XML_daily.asp");

            if (doc != null) {
                NodeList valutes = doc.getFirstChild().getChildNodes();
                for (int i = 0; i < valutes.getLength(); i++) {
                    Node nValute = valutes.item(i);
                    Element eValute = (Element) nValute;
                    int numCode = Integer.parseInt(eValute.getElementsByTagName("NumCode").item(0).getTextContent());
                    String charCode = eValute.getElementsByTagName("CharCode").item(0).getTextContent();
                    int nominal = Integer.parseInt(eValute.getElementsByTagName("Nominal").item(0).getTextContent());
                    String name = eValute.getElementsByTagName("Name").item(0).getTextContent();
                    double rubValue = Double.parseDouble(eValute.getElementsByTagName("Value").item(0).getTextContent().replace(',', '.'));
                    currencies.add(new Currency(numCode, charCode, nominal, name, rubValue));
                }
            }

            return currencies;
        }

        private Document getXmlDoc(String url) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document doc = null;
            try {
                doc = dbf.newDocumentBuilder().parse(new URL(url).openStream());
            } catch (ParserConfigurationException | IOException | SAXException e) {
                Log.d("CCLog", e.getMessage());
            }

            return doc;
        }

        @Override
        protected void onPostExecute(List<Currency> currencies) {
            if (callback != null) {
                callback.onLoad(currencies);
            }
            data.addAll(currencies);
            Log.d("CCLog", "data size: " + data.size());
        }
    }

    public interface LoadCurrenciesCallback {
        void onLoad(List<Currency> currencies);
    }
}
