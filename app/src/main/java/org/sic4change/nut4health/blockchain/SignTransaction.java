/**
 * SignTransaction.java
 * COPYRIGHT: FUNDACIÓN TECNALIA RESEARCH & INNOVATION, 2022.
 * Licensed to SIC4CHANGE under the following conditions: non-exclusive, irrevocable, non-transferable, and non-sublicensable.
 * This license is effective without end date in the field of traceability of patients at risk of malnutrition.
 */

 package org.sic4change.nut4health.blockchain;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.sic4change.nut4health.blockchain.utils.CommonProperties;
import org.sic4change.nut4health.blockchain.utils.Loaders;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.DefaultBlockParameterName;

import at.favre.lib.bytes.Bytes;


public class SignTransaction {
   public static void main(String[] args) throws Exception {
      if (args.length < 2) {
         System.err.println("Error: you should provide the following arguments:");
         System.err.println("\t1. Diagnosis ID");
         System.err.println("\t2. Heath centre ID");
         System.exit(0);
      }

      // Or we could ask it in the System.in
      var diagnosisId = args[0];
      var healthCentreId = args[1];

      // var cred = Loaders.loadCredentialsFromPrivateKey();
      var cred = Loaders.loadCredentialsFromFile();
      var web3j = Loaders.loadWeb3jInstance();

      var nut4health = Loaders.loadNut4HealthContract();
      var encodedFunction = nut4health.registerDiagnosis(diagnosisId, healthCentreId).encodeFunctionCall();

      // Another low level alternative:
      //var function = new Function("registerDiagnosis", Arrays.<Type>asList(new Utf8String("elquesea"), new Utf8String("healthcenter4")), Collections.<TypeReference<?>>emptyList());
      //var encodedFunction = FunctionEncoder.encode(function);

      // Constant for each deployment
      var chainId = web3j.ethChainId().send().getChainId();

      // Can be cached in the app if the signing account is not used externally
      var nonce = web3j.ethGetTransactionCount(cred.getAddress(), DefaultBlockParameterName.LATEST)
                     .sendAsync().get().getTransactionCount();

      var transaction = RawTransaction.createTransaction(nonce,  BigInteger.ZERO, BigInteger.valueOf(0xfffff), CommonProperties.CONTRACT_ADDRESS, encodedFunction);
      var signedTx = TransactionEncoder.signMessage(transaction, chainId.longValue(), cred);

      System.out.println(Bytes.wrap(signedTx).encodeHex());
      System.exit(0);
   }
}