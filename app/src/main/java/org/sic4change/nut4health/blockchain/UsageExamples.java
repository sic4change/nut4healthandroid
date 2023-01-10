/**
 * UsageExamples.java
 * COPYRIGHT: FUNDACIÓN TECNALIA RESEARCH & INNOVATION, 2022.
 * Licensed to SIC4CHANGE under the following conditions: non-exclusive, irrevocable, non-transferable, and non-sublicensable.
 * This license is effective without end date in the field of traceability of patients at risk of malnutrition.
 */

package org.sic4change.nut4health.blockchain;

import org.sic4change.nut4health.blockchain.utils.Loaders;
import org.sic4change.nut4health.blockchain.utils.RevertReasonExtractor;
import org.web3j.protocol.exceptions.TransactionException;

public class UsageExamples {
   public static void main(String[] args) throws Exception {
      var nut4health = Loaders.loadNut4HealthContract();
      System.out.println("Contract address: " + nut4health.getContractAddress());
      
      // Call example
      var screenerRoleId = nut4health.ROLE_SCREENER().send();
      System.out.println("Has screener role: " + nut4health.hasRole(screenerRoleId, "0x1aa49faa8136d6ade31519ab2606996acd46af51").send());

      // Transaction example
      try {
         var txReceipt = nut4health.registerDiagnosis(String.format("diagnosis-%n", System.currentTimeMillis()), "test_hc").send();
         System.out.println("Transaction mined in block number " + txReceipt.getBlockNumber());
      } catch (TransactionException err) {
         System.out.println("Transaction reverted: " + RevertReasonExtractor.revertReasonToAscii(err.getTransactionReceipt().get()));
      }
   }
}
