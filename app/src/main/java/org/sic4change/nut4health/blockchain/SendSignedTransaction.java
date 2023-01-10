/**
 * SendSignedTransaction.java
 * COPYRIGHT: FUNDACIÓN TECNALIA RESEARCH & INNOVATION, 2022.
 * Licensed to SIC4CHANGE under the following conditions: non-exclusive, irrevocable, non-transferable, and non-sublicensable.
 * This license is effective without end date in the field of traceability of patients at risk of malnutrition.
 */

package org.sic4change.nut4health.blockchain;

import org.sic4change.nut4health.blockchain.utils.Loaders;
import org.sic4change.nut4health.blockchain.utils.RevertReasonExtractor;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;


public class SendSignedTransaction {
   public static void main(String[] args) throws Exception {
      if (args.length < 1) {
         System.err.println("Error: you should provide the signed transaction as the first argument");
         System.exit(0);
      }

      // Or we could ask it in the System.in
      var signedTransaction = args[0];

      var web3j = Loaders.loadWeb3jInstance();

      System.out.println("Sending transaction...");
      var tx = web3j.ethSendRawTransaction(signedTransaction).send();

      if (tx.hasError()) {
         System.err.println("Error: " + tx.getError().getMessage());
         System.exit(-1);
      }

      System.out.println("Tx hash: " + tx.getTransactionHash());

      var receiptProcessor = new PollingTransactionReceiptProcessor(
         web3j,
         TransactionManager.DEFAULT_POLLING_FREQUENCY,
         TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
      );

      var txReceipt = receiptProcessor.waitForTransactionReceipt(tx.getTransactionHash());
      if (txReceipt.getRevertReason() != null) {
         System.out.println("Transaction reverted: " + RevertReasonExtractor.revertReasonToAscii(txReceipt));
      } else {
         System.out.println("Successful transaction");
      }

      System.exit(0);
   }
}