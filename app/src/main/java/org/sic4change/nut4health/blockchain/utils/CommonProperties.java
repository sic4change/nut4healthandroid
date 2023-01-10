/**
 * CommonProperties.java
 * COPYRIGHT: FUNDACIÓN TECNALIA RESEARCH & INNOVATION, 2022.
 * Licensed to SIC4CHANGE under the following conditions: non-exclusive, irrevocable, non-transferable, and non-sublicensable.
 * This license is effective without end date in the field of traceability of patients at risk of malnutrition.
 */

package org.sic4change.nut4health.blockchain.utils;

public class CommonProperties {
   
   public static final String CONTRACT_ADDRESS = System.getenv().getOrDefault("CONTRACT_ADDR", "0x73F607699218dC2c3174403dd6008F5bE0964D6D");
   static final String NODE_URL = System.getenv().getOrDefault("WEB3J_NODE_URL", "https://node1.ethereum.bclab.dev");

   static final String WALLET_PASSWORD = System.getenv().getOrDefault("WEB3J_WALLET_PASSWORD", "");
   static final String WALLET_PATH = System.getenv().getOrDefault("WEB3J_WALLET_PATH", "/Users/aaronasencio/SIC4Change/NUT4Health/Blockchain/javascript-integration-main/credentials/screener.json");
}
