/**
 * RevertReasonExtractor.java
 * COPYRIGHT: FUNDACIÓN TECNALIA RESEARCH & INNOVATION, 2022.
 * Licensed to SIC4CHANGE under the following conditions: non-exclusive, irrevocable, non-transferable, and non-sublicensable.
 * This license is effective without end date in the field of traceability of patients at risk of malnutrition.
 */

package org.sic4change.nut4health.blockchain.utils;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

public class RevertReasonExtractor {
	private static String hexToAscii(String hexStr) {
		var output = new StringBuilder("");
		
		for (int i = 0; i < hexStr.length(); i += 2) {
			  String str = hexStr.substring(i, i + 2);
			  output.append((char) Integer.parseInt(str, 16));
		}
		
		return output.toString();
	 }
  
	 // Example from https://ethereum.stackexchange.com/a/66173 which explains why 138 chars should be removed.
	 // 0x08c379a0                                                       // Function selector
	 // 0000000000000000000000000000000000000000000000000000000000000020 // Offset of string return value
	 // 0000000000000000000000000000000000000000000000000000000000000047 // Length of string return value (the revert reason)
	 // 6d7946756e6374696f6e206f6e6c79206163636570747320617267756d656e74 // first 32 bytes of the revert reason
	 // 7320776869636820617265206772656174686572207468616e206f7220657175 // next 32 bytes of the revert reason
	 // 616c20746f203500000000000000000000000000000000000000000000000000 // last 7 bytes of the revert reason
	 private static String revertReasonToAscii(String hexStr) {
		var revertReasonIndex =  hexStr.startsWith("0x")? 138 : 136;
		return hexToAscii(hexStr.substring(revertReasonIndex));
	}

	public static String revertReasonToAscii(TransactionReceipt txReceipt) {
		return revertReasonToAscii(txReceipt.getRevertReason());
	}
}
