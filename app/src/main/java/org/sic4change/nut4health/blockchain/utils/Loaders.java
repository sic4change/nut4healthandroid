/**
 * Loaders.java
 * COPYRIGHT: FUNDACIÓN TECNALIA RESEARCH & INNOVATION, 2022.
 * Licensed to SIC4CHANGE under the following conditions: non-exclusive, irrevocable, non-transferable, and non-sublicensable.
 * This license is effective without end date in the field of traceability of patients at risk of malnutrition.
 */

package org.sic4change.nut4health.blockchain.utils;

import android.content.Context;
import android.content.res.AssetManager;

import org.web3j.protocol.Web3j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Scanner;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

public class Loaders {

   private Loaders() {}

   public static Web3j loadWeb3jInstance() {
      return Web3j.build(new HttpService(CommonProperties.NODE_URL));
   }

   public static Credentials loadCredentialsFromPrivateKey() {
      var inputReader = new Scanner(System.in);
      try {
         System.out.println("Please enter private key:");
         final String privateKey = inputReader.nextLine();
         return Credentials.create(privateKey);
      } finally {
         inputReader.close();
      }
   }

   public static Credentials loadCredentialsFromFile(Context context) throws IOException, CipherException {
      //return WalletUtils.loadCredentials(CommonProperties.WALLET_PASSWORD, CommonProperties.WALLET_PATH);

      //File f = new File(context.getAssets(), "screener.json");
      AssetManager assetManager = context.getAssets();
      InputStream input = assetManager.open("screener.json");
      File tempFile = File.createTempFile("screener", ".json");
      FileOutputStream fos = new FileOutputStream(tempFile);
      byte[] buffer = new byte[1024];
      int read;
      while((read = input.read(buffer)) != -1) {
         fos.write(buffer, 0, read);
      }
      fos.close();
      input.close();
      return WalletUtils.loadCredentials(CommonProperties.WALLET_PASSWORD, tempFile);
   }

   public static Nut4Health loadNut4HealthContract(Context context) throws IOException, CipherException {
      var credentials = loadCredentialsFromFile(context);
      var web3j = loadWeb3jInstance();

      // System.out.println("Deploying Nut4Health contract ...");
      // var nut4health = Nut4Health.deploy(web3j, credentials, new DefaultGasProvider()).send();

      return Nut4Health.load(CommonProperties.CONTRACT_ADDRESS, web3j, credentials, new StaticGasProvider(BigInteger.ZERO, BigInteger.valueOf(0xfffff)));
   }
}
