package com.testtask.currencyconverter.model;

import android.os.AsyncTask;
import android.util.Log;

import com.testtask.currencyconverter.common.Currency;

import org.w3c.dom.Document;
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
                    NodeList valuteParams = valutes.item(i).getChildNodes();
                    String[] currencyParameters = new String[5];
                    for (int j = 0; j < valuteParams.getLength(); j++) {
                        currencyParameters[j] = valuteParams.item(j).getFirstChild().getNodeValue();
                    }
                    currencyParameters[4] = currencyParameters[4].replace(',', '.');
                    currencies.add(new Currency(Integer.parseInt(currencyParameters[0]), currencyParameters[1],
                            Integer.parseInt(currencyParameters[2]), currencyParameters[3], Double.parseDouble(currencyParameters[4])));
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
