package org.sic4change.nut4health.blockchain.utils;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.9.4.
 */
@SuppressWarnings("rawtypes")
public class Nut4Health extends Contract {
    public static final String BINARY = "0x60e0604052600060809081527f4d61b4f17391ed14d6009f73638b7132d51a652884f17d3aca79a56d17053a6c60a0526000805160206200205a83398151915260c0526200005290600290600362000283565b506006805463ffffffff60a01b1916600160a01b1790553480156200007657600080fd5b5062000084600033620000d3565b620000b17f4d61b4f17391ed14d6009f73638b7132d51a652884f17d3aca79a56d17053a6c6000620000e3565b620000cd6000805160206200205a8339815191526000620000e3565b620002ea565b620000df82826200012e565b5050565b600082815260208190526040808220600101805490849055905190918391839186917fbd79b86ffe0ab8e8776151514217cd7cacd52c909f66475c3af44e129f0b00ff9190a4505050565b6200014582826200017160201b62000def1760201c565b60008281526001602090815260409091206200016c91839062000e7462000211821b17901c565b505050565b6000828152602081815260408083206001600160a01b038516845290915290205460ff16620000df576000828152602081815260408083206001600160a01b03851684529091529020805460ff19166001179055620001cd3390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45050565b600062000228836001600160a01b03841662000231565b90505b92915050565b60008181526001830160205260408120546200027a575081546001818101845560008481526020808220909301849055845484825282860190935260409020919091556200022b565b5060006200022b565b828054828255906000526020600020908101928215620002c1579160200282015b82811115620002c1578251825591602001919060010190620002a4565b50620002cf929150620002d3565b5090565b5b80821115620002cf5760008155600101620002d4565b611d6080620002fa6000396000f3fe608060405234801561001057600080fd5b50600436106101585760003560e01c806372e3b7ed116100c3578063cc8fa12b1161007c578063cc8fa12b14610320578063cda26e2214610333578063d391014b14610305578063d547741f14610346578063e7ff17a714610359578063f44aef831461036c57600080fd5b806372e3b7ed1461028d5780639010d07c146102a057806391d14854146102cb5780639a6f744d146102de578063a217fddf14610305578063ca15c8731461030d57600080fd5b8063248a9ca311610115578063248a9ca3146101f45780632895d917146102175780632f2ff15d1461023f57806336568abe146102525780634b109c6014610265578063630f2a8d1461027857600080fd5b806301ffc9a71461015d578063029730be1461018557806318303c8d1461019a5780631d78770b146101ad5780631da26a8b146101ce578063217ec4bf146101e1575b600080fd5b61017061016b3660046117a9565b61037f565b60405190151581526020015b60405180910390f35b61019861019336600461181c565b6103aa565b005b6101986101a836600461181c565b610491565b6101c06101bb36600461185e565b610578565b60405190815260200161017c565b6101986101dc366004611893565b610599565b6101986101ef3660046118c6565b610614565b6101c061020236600461185e565b60009081526020819052604090206001015490565b61022a61022536600461181c565b61074f565b60405163ffffffff909116815260200161017c565b61019861024d366004611919565b610791565b610198610260366004611919565b6107bb565b610198610273366004611950565b610809565b6101c0600080516020611d0b83398151915281565b61019861029b36600461197a565b6108d8565b6102b36102ae366004611996565b610948565b6040516001600160a01b03909116815260200161017c565b6101706102d9366004611919565b610967565b6101c07f4d61b4f17391ed14d6009f73638b7132d51a652884f17d3aca79a56d17053a6c81565b6101c0600081565b6101c061031b36600461185e565b610990565b61019861032e36600461181c565b6109a7565b61019861034136600461181c565b610ab2565b610198610354366004611919565b610b29565b6101986103673660046119b8565b610b3e565b61019861037a3660046119d3565b610c17565b60006001600160e01b03198216635a05180f60e01b14806103a457506103a482610e89565b92915050565b60006103b581610ebe565b6000600460006103c58686610ecb565b81526020810191909152604001600020805490915060ff16156104465760405162461bcd60e51b815260206004820152602e60248201527f546865726520697320616c726561647920612063656e7472652077697468207460448201526d1a19481c1c9bdd9a59195908125160921b60648201526084015b60405180910390fd5b805460ff191660011781556040517fb743936c4bd7bb7bcb51834f504efdcb7b9c7837cae8c36ffda336a6333cdd57906104839086908690611a68565b60405180910390a150505050565b600080516020611d0b8339815191526104a981610ebe565b6000600960006104b98686610ecb565b815260200190815260200160002090506104d281610f57565b80546002820154600091610502916101009091046001600160a01b031690600160a01b900463ffffffff16611084565b9050801561051a57815460ff19166004178255610526565b815460ff191660031782555b60028201546040517ff51648553b508293ca63c0634205f0d54746cd810f1e411fc607f52902482a509161056991889188916001600160a01b0390911690611a92565b60405180910390a15050505050565b6002818154811061058857600080fd5b600091825260209091200154905081565b60006105a481610ebe565b600580546001600160a01b038581166001600160a01b03199283168117909355600680549186169190921681179091556040805192835260208301919091527f0b1186973f810894b87ab0bfbee422fddcaad21b46dc705a561451bbb6bac11791015b60405180910390a1505050565b600061061f81610ebe565b6106298383610ecb565b6001600160a01b03851660009081526003602090815260408083208490559282526004905220805460ff1661069b5760405162461bcd60e51b8152602060048201526018602482015277556e6578697374696e67206865616c74682063656e74726560401b604482015260640161043d565b6106b3600080516020611d0b83398151915286610967565b61071c5760405162461bcd60e51b815260206004820152603460248201527f5468652061737369676e6564206163636f756e74206d75737420686176652061604482015273206865616c7468207365727669636520726f6c6560601b606482015260840161043d565b7fce8bb596c23923151a618113e7825dc7c5519a2068d713d613b434e9707f762285858560405161056993929190611abe565b600080600960006107608686610ecb565b81526020810191909152604001600020805490915060ff16600481111561078957610789611a7c565b949350505050565b6000828152602081905260409020600101546107ac81610ebe565b6107b68383611154565b505050565b60006107c681610ebe565b6107d08383611176565b6107e8600080516020611d0b83398151915283610967565b156107b657506001600160a01b031660009081526003602052604081205550565b600061081481610ebe565b61083e7f4d61b4f17391ed14d6009f73638b7132d51a652884f17d3aca79a56d17053a6c84610967565b61089a5760405162461bcd60e51b815260206004820152602760248201527f5468652070726f7669646564206163636f756e74206973206e6f7420616e207360448201526631b932b2b732b960c91b606482015260840161043d565b6108a3826111f0565b50506001600160a01b03919091166000908152600860205260409020805463ffffffff191663ffffffff909216919091179055565b60006108e381610ebe565b60006108ee846111f0565b805464ffffffff00191661010063ffffffff86811691820292909217835560408051928816835260208301919091529192507ff4117a4b26f252036dcc957232653248eec984154abc62c890092e7d15de1b729101610483565b60008281526001602052604081206109609083611256565b9392505050565b6000918252602082815260408084206001600160a01b0393909316845291905290205460ff1690565b60008181526001602052604081206103a490611262565b60006109b281610ebe565b6000600960006109c28686610ecb565b8152602081019190915260400160002090506003815460ff1660048111156109ec576109ec611a7c565b14610a495760405162461bcd60e51b815260206004820152602760248201527f446961676e6f736973206d75737420626520696e202776616c696461746564276044820152662073746174757360c81b606482015260840161043d565b805460ff19166004178082556002820154610a81916001600160a01b03610100909104169063ffffffff600160a01b9091041661126c565b7f5328d4237ddebff0210656d6532608059de6c60e66087bd8862da9bb9f495e958484604051610483929190611a68565b600060096000610ac28585610ecb565b81526020019081526020016000209050610adb81610f57565b805460ff1916600290811782558101546040517fcf28bf7af75d8a2786d49419943575c740769d997ddeb54ab2b57f7335b3e31e9161060791869186916001600160a01b0390911690611a92565b6000610b3481610ebe565b6107d083836112ed565b6000610b4981610ebe565b6006805463ffffffff600160a01b9182900481166000908152600760205260409081902080548784166101000264ffffffffff19909116176001178155935490517ff4117a4b26f252036dcc957232653248eec984154abc62c890092e7d15de1b7293610bcf93920490911690869063ffffffff92831681529116602082015260400190565b60405180910390a1600654610bf290600160a01b900463ffffffff166001611b02565b600660146101000a81548163ffffffff021916908363ffffffff160217905550505050565b7f4d61b4f17391ed14d6009f73638b7132d51a652884f17d3aca79a56d17053a6c610c4181610ebe565b6000610c4d8484610ecb565b60008181526004602052604090205490915060ff16610ca95760405162461bcd60e51b8152602060048201526018602482015277556e6578697374696e67206865616c74682063656e74726560401b604482015260640161043d565b600060096000610cb98989610ecb565b8152602081019190915260400160009081209150815460ff166004811115610ce357610ce3611a7c565b14610d305760405162461bcd60e51b815260206004820152601c60248201527f416c7265616479207265676973746572656420646961676e6f73697300000000604482015260640161043d565b805460018083018490556101003381026001600160a81b03199093169290921717808355610d689190046001600160a01b0316611312565b5460028201805463ffffffff60a01b19166101009283900463ffffffff908116600160a01b908102929092179283905584546040517f5a94c26b5554108cd8d0c7318cc1089d703ae4eeb47cc44c5d9340f42127206395610dde958e958e95929094046001600160a01b03169390041690611b1f565b60405180910390a150505050505050565b610df98282610967565b610e70576000828152602081815260408083206001600160a01b03851684529091529020805460ff19166001179055610e2f3390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45b5050565b6000610960836001600160a01b03841661139f565b60006001600160e01b03198216637965db0b60e01b14806103a457506301ffc9a760e01b6001600160e01b03198316146103a4565b610ec881336113ee565b50565b6000818103610f265760405162461bcd60e51b815260206004820152602160248201527f416e20656d70747920737472696e67206973206e6f7420612076616c696420696044820152601960fa1b606482015260840161043d565b8282604051602001610f39929190611b5a565b60405160208183030381529060405280519060200120905092915050565b600080516020611d0b833981519152610f6f81610ebe565b6001825460ff166004811115610f8757610f87611a7c565b14610fe55760405162461bcd60e51b815260206004820152602860248201527f446961676e6f736973206d75737420626520696e202772656769737465726564604482015267272073746174757360c01b606482015260840161043d565b6002820180546001600160a01b0319163390811790915560018301546000918252600360205260409091205414610e705760405162461bcd60e51b815260206004820152603760248201527f4865616c74682073657276696365206e6f742061737369676e656420746f206460448201527f6961676e6f73697327206865616c74682063656e747265000000000000000000606482015260840161043d565b6005546000906001600160a01b031661109f575060006103a4565b6005546006546040516001600160a01b039182166024820152858216604482015260648101859052600092919091169060840160408051601f198184030181529181526020820180516001600160e01b03166323b872dd60e01b179052516111079190611b8e565b6000604051808303816000865af19150503d8060008114611144576040519150601f19603f3d011682016040523d82523d6000602084013e611149565b606091505b509095945050505050565b61115e8282610def565b60008281526001602052604090206107b69082610e74565b6001600160a01b03811633146111e65760405162461bcd60e51b815260206004820152602f60248201527f416363657373436f6e74726f6c3a2063616e206f6e6c792072656e6f756e636560448201526e103937b632b9903337b91039b2b63360891b606482015260840161043d565b610e708282611452565b63ffffffff81166000908152600760205260408120805460ff166103a45760405162461bcd60e51b815260206004820152601e60248201527f4e6f207061796d656e7420636f6e66696775726174696f6e20666f756e640000604482015260640161043d565b60006109608383611474565b60006103a4825490565b6005546006546040516323b872dd60e01b81526001600160a01b0391821660048201528482166024820152604481018490529116906323b872dd906064016020604051808303816000875af11580156112c9573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906107b69190611baa565b60008281526020819052604090206001015461130881610ebe565b6107b68383611452565b6001600160a01b03811660009081526008602052604081205463ffffffff168082036113965760405162461bcd60e51b815260206004820152602d60248201527f4e6f207061796d656e7420636f6e66696775726174696f6e2061737369676e6560448201526c32103a379039b1b932b2b732b960991b606482015260840161043d565b610960816111f0565b60008181526001830160205260408120546113e6575081546001818101845560008481526020808220909301849055845484825282860190935260409020919091556103a4565b5060006103a4565b6113f88282610967565b610e7057611410816001600160a01b0316601461149e565b61141b83602061149e565b60405160200161142c929190611bcc565b60408051601f198184030181529082905262461bcd60e51b825261043d91600401611c41565b61145c828261163a565b60008281526001602052604090206107b6908261169f565b600082600001828154811061148b5761148b611c74565b9060005260206000200154905092915050565b606060006114ad836002611c8a565b6114b8906002611ca1565b67ffffffffffffffff8111156114d0576114d0611cb4565b6040519080825280601f01601f1916602001820160405280156114fa576020820181803683370190505b509050600360fc1b8160008151811061151557611515611c74565b60200101906001600160f81b031916908160001a905350600f60fb1b8160018151811061154457611544611c74565b60200101906001600160f81b031916908160001a9053506000611568846002611c8a565b611573906001611ca1565b90505b60018111156115eb576f181899199a1a9b1b9c1cb0b131b232b360811b85600f16601081106115a7576115a7611c74565b1a60f81b8282815181106115bd576115bd611c74565b60200101906001600160f81b031916908160001a90535060049490941c936115e481611cca565b9050611576565b5083156109605760405162461bcd60e51b815260206004820181905260248201527f537472696e67733a20686578206c656e67746820696e73756666696369656e74604482015260640161043d565b6116448282610967565b15610e70576000828152602081815260408083206001600160a01b0385168085529252808320805460ff1916905551339285917ff6391f5c32d9c69d2a47ea670b442974b53935d1edc7fd64eb21e047a839171b9190a45050565b6000610960836001600160a01b038416600081815260018301602052604081205480156117985760006116d3600183611ce1565b85549091506000906116e790600190611ce1565b905081811461174c57600086600001828154811061170757611707611c74565b906000526020600020015490508087600001848154811061172a5761172a611c74565b6000918252602080832090910192909255918252600188019052604090208390555b855486908061175d5761175d611cf4565b6001900381819060005260206000200160009055905585600101600086815260200190815260200160002060009055600193505050506103a4565b60009150506103a4565b5092915050565b6000602082840312156117bb57600080fd5b81356001600160e01b03198116811461096057600080fd5b60008083601f8401126117e557600080fd5b50813567ffffffffffffffff8111156117fd57600080fd5b60208301915083602082850101111561181557600080fd5b9250929050565b6000806020838503121561182f57600080fd5b823567ffffffffffffffff81111561184657600080fd5b611852858286016117d3565b90969095509350505050565b60006020828403121561187057600080fd5b5035919050565b80356001600160a01b038116811461188e57600080fd5b919050565b600080604083850312156118a657600080fd5b6118af83611877565b91506118bd60208401611877565b90509250929050565b6000806000604084860312156118db57600080fd5b6118e484611877565b9250602084013567ffffffffffffffff81111561190057600080fd5b61190c868287016117d3565b9497909650939450505050565b6000806040838503121561192c57600080fd5b823591506118bd60208401611877565b803563ffffffff8116811461188e57600080fd5b6000806040838503121561196357600080fd5b61196c83611877565b91506118bd6020840161193c565b6000806040838503121561198d57600080fd5b61196c8361193c565b600080604083850312156119a957600080fd5b50508035926020909101359150565b6000602082840312156119ca57600080fd5b6109608261193c565b600080600080604085870312156119e957600080fd5b843567ffffffffffffffff80821115611a0157600080fd5b611a0d888389016117d3565b90965094506020870135915080821115611a2657600080fd5b50611a33878288016117d3565b95989497509550505050565b81835281816020850137506000828201602090810191909152601f909101601f19169091010190565b602081526000610789602083018486611a3f565b634e487b7160e01b600052602160045260246000fd5b604081526000611aa6604083018587611a3f565b905060018060a01b0383166020830152949350505050565b6001600160a01b0384168152604060208201819052600090611ae39083018486611a3f565b95945050505050565b634e487b7160e01b600052601160045260246000fd5b63ffffffff8181168382160190808211156117a2576117a2611aec565b606081526000611b33606083018688611a3f565b6001600160a01b039490941660208301525063ffffffff9190911660409091015292915050565b8183823760009101908152919050565b60005b83811015611b85578181015183820152602001611b6d565b50506000910152565b60008251611ba0818460208701611b6a565b9190910192915050565b600060208284031215611bbc57600080fd5b8151801515811461096057600080fd5b7f416363657373436f6e74726f6c3a206163636f756e7420000000000000000000815260008351611c04816017850160208801611b6a565b7001034b99036b4b9b9b4b733903937b6329607d1b6017918401918201528351611c35816028840160208801611b6a565b01602801949350505050565b6020815260008251806020840152611c60816040850160208701611b6a565b601f01601f19169190910160400192915050565b634e487b7160e01b600052603260045260246000fd5b80820281158282048414176103a4576103a4611aec565b808201808211156103a4576103a4611aec565b634e487b7160e01b600052604160045260246000fd5b600081611cd957611cd9611aec565b506000190190565b818103818111156103a4576103a4611aec565b634e487b7160e01b600052603160045260246000fdfedee418914a796929f14c4e11785f4a1c3a6e4b4d8b3356de31ad4179de0998d9a2646970667358221220612f824131c9a53bad024f637e84847d9d604643397ae17c0d4175dca82e18ae64736f6c63430008110033dee418914a796929f14c4e11785f4a1c3a6e4b4d8b3356de31ad4179de0998d9";

    public static final String FUNC_DEFAULT_ADMIN_ROLE = "DEFAULT_ADMIN_ROLE";

    public static final String FUNC_ROLES = "ROLES";

    public static final String FUNC_ROLE_ADMIN = "ROLE_ADMIN";

    public static final String FUNC_ROLE_HEALTH_SERVICE = "ROLE_HEALTH_SERVICE";

    public static final String FUNC_ROLE_SCREENER = "ROLE_SCREENER";

    public static final String FUNC_ADDPAYMENTCONFIGURATION = "addPaymentConfiguration";

    public static final String FUNC_ASSIGNTOHEALTHCENTRE = "assignToHealthCentre";

    public static final String FUNC_CREATEHEALTHCENTRE = "createHealthCentre";

    public static final String FUNC_GETDIAGNOSISDETAILS = "getDiagnosisDetails";

    public static final String FUNC_GETROLEADMIN = "getRoleAdmin";

    public static final String FUNC_GETROLEMEMBER = "getRoleMember";

    public static final String FUNC_GETROLEMEMBERCOUNT = "getRoleMemberCount";

    public static final String FUNC_GRANTROLE = "grantRole";

    public static final String FUNC_HASROLE = "hasRole";

    public static final String FUNC_INVALIDATEDIAGNOSIS = "invalidateDiagnosis";

    public static final String FUNC_PAYREWARD = "payReward";

    public static final String FUNC_REGISTERDIAGNOSIS = "registerDiagnosis";

    public static final String FUNC_RENOUNCEROLE = "renounceRole";

    public static final String FUNC_REVOKEROLE = "revokeRole";

    public static final String FUNC_SETSCREENERCONFIGURATION = "setScreenerConfiguration";

    public static final String FUNC_SETTOKEN = "setToken";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_UPDATEPRICE = "updatePrice";

    public static final String FUNC_VALIDATEDIAGNOSIS = "validateDiagnosis";

    public static final Event DIAGNOSISCREATED_EVENT = new Event("DiagnosisCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint32>() {}));
    ;

    public static final Event DIAGNOSISINVALIDATED_EVENT = new Event("DiagnosisInvalidated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event DIAGNOSISPAID_EVENT = new Event("DiagnosisPaid", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event DIAGNOSISVALIDATED_EVENT = new Event("DiagnosisValidated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event HEALTHCENTRECREATED_EVENT = new Event("HealthCentreCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event HEALTHSERVICEASSIGNED_EVENT = new Event("HealthServiceAssigned", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event PAYMENTUPDATED_EVENT = new Event("PaymentUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}, new TypeReference<Uint32>() {}));
    ;

    public static final Event ROLEADMINCHANGED_EVENT = new Event("RoleAdminChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event ROLEGRANTED_EVENT = new Event("RoleGranted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event ROLEREVOKED_EVENT = new Event("RoleRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TOKENUPDATED_EVENT = new Event("TokenUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("1337", "0x73F607699218dC2c3174403dd6008F5bE0964D6D");
    }

    @Deprecated
    protected Nut4Health(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Nut4Health(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Nut4Health(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Nut4Health(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<DiagnosisCreatedEventResponse> getDiagnosisCreatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DIAGNOSISCREATED_EVENT, transactionReceipt);
        ArrayList<DiagnosisCreatedEventResponse> responses = new ArrayList<DiagnosisCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DiagnosisCreatedEventResponse typedResponse = new DiagnosisCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.diagnosisId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.screener = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.reward = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DiagnosisCreatedEventResponse> diagnosisCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DiagnosisCreatedEventResponse>() {
            @Override
            public DiagnosisCreatedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(DIAGNOSISCREATED_EVENT, log);
                DiagnosisCreatedEventResponse typedResponse = new DiagnosisCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.diagnosisId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.screener = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.reward = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DiagnosisCreatedEventResponse> diagnosisCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DIAGNOSISCREATED_EVENT));
        return diagnosisCreatedEventFlowable(filter);
    }

    public static List<DiagnosisInvalidatedEventResponse> getDiagnosisInvalidatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DIAGNOSISINVALIDATED_EVENT, transactionReceipt);
        ArrayList<DiagnosisInvalidatedEventResponse> responses = new ArrayList<DiagnosisInvalidatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DiagnosisInvalidatedEventResponse typedResponse = new DiagnosisInvalidatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.diagnosisId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.validator = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DiagnosisInvalidatedEventResponse> diagnosisInvalidatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DiagnosisInvalidatedEventResponse>() {
            @Override
            public DiagnosisInvalidatedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(DIAGNOSISINVALIDATED_EVENT, log);
                DiagnosisInvalidatedEventResponse typedResponse = new DiagnosisInvalidatedEventResponse();
                typedResponse.log = log;
                typedResponse.diagnosisId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.validator = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DiagnosisInvalidatedEventResponse> diagnosisInvalidatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DIAGNOSISINVALIDATED_EVENT));
        return diagnosisInvalidatedEventFlowable(filter);
    }

    public static List<DiagnosisPaidEventResponse> getDiagnosisPaidEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DIAGNOSISPAID_EVENT, transactionReceipt);
        ArrayList<DiagnosisPaidEventResponse> responses = new ArrayList<DiagnosisPaidEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DiagnosisPaidEventResponse typedResponse = new DiagnosisPaidEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.diagnosisId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DiagnosisPaidEventResponse> diagnosisPaidEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DiagnosisPaidEventResponse>() {
            @Override
            public DiagnosisPaidEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(DIAGNOSISPAID_EVENT, log);
                DiagnosisPaidEventResponse typedResponse = new DiagnosisPaidEventResponse();
                typedResponse.log = log;
                typedResponse.diagnosisId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DiagnosisPaidEventResponse> diagnosisPaidEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DIAGNOSISPAID_EVENT));
        return diagnosisPaidEventFlowable(filter);
    }

    public static List<DiagnosisValidatedEventResponse> getDiagnosisValidatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DIAGNOSISVALIDATED_EVENT, transactionReceipt);
        ArrayList<DiagnosisValidatedEventResponse> responses = new ArrayList<DiagnosisValidatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DiagnosisValidatedEventResponse typedResponse = new DiagnosisValidatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.diagnosisId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.validator = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DiagnosisValidatedEventResponse> diagnosisValidatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DiagnosisValidatedEventResponse>() {
            @Override
            public DiagnosisValidatedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(DIAGNOSISVALIDATED_EVENT, log);
                DiagnosisValidatedEventResponse typedResponse = new DiagnosisValidatedEventResponse();
                typedResponse.log = log;
                typedResponse.diagnosisId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.validator = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DiagnosisValidatedEventResponse> diagnosisValidatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DIAGNOSISVALIDATED_EVENT));
        return diagnosisValidatedEventFlowable(filter);
    }

    public static List<HealthCentreCreatedEventResponse> getHealthCentreCreatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(HEALTHCENTRECREATED_EVENT, transactionReceipt);
        ArrayList<HealthCentreCreatedEventResponse> responses = new ArrayList<HealthCentreCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            HealthCentreCreatedEventResponse typedResponse = new HealthCentreCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.healthCentreId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<HealthCentreCreatedEventResponse> healthCentreCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, HealthCentreCreatedEventResponse>() {
            @Override
            public HealthCentreCreatedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(HEALTHCENTRECREATED_EVENT, log);
                HealthCentreCreatedEventResponse typedResponse = new HealthCentreCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.healthCentreId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<HealthCentreCreatedEventResponse> healthCentreCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(HEALTHCENTRECREATED_EVENT));
        return healthCentreCreatedEventFlowable(filter);
    }

    public static List<HealthServiceAssignedEventResponse> getHealthServiceAssignedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(HEALTHSERVICEASSIGNED_EVENT, transactionReceipt);
        ArrayList<HealthServiceAssignedEventResponse> responses = new ArrayList<HealthServiceAssignedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            HealthServiceAssignedEventResponse typedResponse = new HealthServiceAssignedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.healthService = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.healthCentreId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<HealthServiceAssignedEventResponse> healthServiceAssignedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, HealthServiceAssignedEventResponse>() {
            @Override
            public HealthServiceAssignedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(HEALTHSERVICEASSIGNED_EVENT, log);
                HealthServiceAssignedEventResponse typedResponse = new HealthServiceAssignedEventResponse();
                typedResponse.log = log;
                typedResponse.healthService = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.healthCentreId = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<HealthServiceAssignedEventResponse> healthServiceAssignedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(HEALTHSERVICEASSIGNED_EVENT));
        return healthServiceAssignedEventFlowable(filter);
    }

    public static List<PaymentUpdatedEventResponse> getPaymentUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PAYMENTUPDATED_EVENT, transactionReceipt);
        ArrayList<PaymentUpdatedEventResponse> responses = new ArrayList<PaymentUpdatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PaymentUpdatedEventResponse typedResponse = new PaymentUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.configurationId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PaymentUpdatedEventResponse> paymentUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PaymentUpdatedEventResponse>() {
            @Override
            public PaymentUpdatedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(PAYMENTUPDATED_EVENT, log);
                PaymentUpdatedEventResponse typedResponse = new PaymentUpdatedEventResponse();
                typedResponse.log = log;
                typedResponse.configurationId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PaymentUpdatedEventResponse> paymentUpdatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAYMENTUPDATED_EVENT));
        return paymentUpdatedEventFlowable(filter);
    }

    public static List<RoleAdminChangedEventResponse> getRoleAdminChangedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ROLEADMINCHANGED_EVENT, transactionReceipt);
        ArrayList<RoleAdminChangedEventResponse> responses = new ArrayList<RoleAdminChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RoleAdminChangedEventResponse typedResponse = new RoleAdminChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.previousAdminRole = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.newAdminRole = (byte[]) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoleAdminChangedEventResponse> roleAdminChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RoleAdminChangedEventResponse>() {
            @Override
            public RoleAdminChangedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ROLEADMINCHANGED_EVENT, log);
                RoleAdminChangedEventResponse typedResponse = new RoleAdminChangedEventResponse();
                typedResponse.log = log;
                typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.previousAdminRole = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.newAdminRole = (byte[]) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoleAdminChangedEventResponse> roleAdminChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEADMINCHANGED_EVENT));
        return roleAdminChangedEventFlowable(filter);
    }

    public static List<RoleGrantedEventResponse> getRoleGrantedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ROLEGRANTED_EVENT, transactionReceipt);
        ArrayList<RoleGrantedEventResponse> responses = new ArrayList<RoleGrantedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RoleGrantedEventResponse typedResponse = new RoleGrantedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoleGrantedEventResponse> roleGrantedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RoleGrantedEventResponse>() {
            @Override
            public RoleGrantedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ROLEGRANTED_EVENT, log);
                RoleGrantedEventResponse typedResponse = new RoleGrantedEventResponse();
                typedResponse.log = log;
                typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoleGrantedEventResponse> roleGrantedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEGRANTED_EVENT));
        return roleGrantedEventFlowable(filter);
    }

    public static List<RoleRevokedEventResponse> getRoleRevokedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ROLEREVOKED_EVENT, transactionReceipt);
        ArrayList<RoleRevokedEventResponse> responses = new ArrayList<RoleRevokedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RoleRevokedEventResponse typedResponse = new RoleRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoleRevokedEventResponse> roleRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RoleRevokedEventResponse>() {
            @Override
            public RoleRevokedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ROLEREVOKED_EVENT, log);
                RoleRevokedEventResponse typedResponse = new RoleRevokedEventResponse();
                typedResponse.log = log;
                typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoleRevokedEventResponse> roleRevokedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEREVOKED_EVENT));
        return roleRevokedEventFlowable(filter);
    }

    public static List<TokenUpdatedEventResponse> getTokenUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TOKENUPDATED_EVENT, transactionReceipt);
        ArrayList<TokenUpdatedEventResponse> responses = new ArrayList<TokenUpdatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TokenUpdatedEventResponse typedResponse = new TokenUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.token = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TokenUpdatedEventResponse> tokenUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TokenUpdatedEventResponse>() {
            @Override
            public TokenUpdatedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TOKENUPDATED_EVENT, log);
                TokenUpdatedEventResponse typedResponse = new TokenUpdatedEventResponse();
                typedResponse.log = log;
                typedResponse.token = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TokenUpdatedEventResponse> tokenUpdatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TOKENUPDATED_EVENT));
        return tokenUpdatedEventFlowable(filter);
    }

    public RemoteFunctionCall<byte[]> DEFAULT_ADMIN_ROLE() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DEFAULT_ADMIN_ROLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> ROLES(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ROLES, 
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> ROLE_ADMIN() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ROLE_ADMIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> ROLE_HEALTH_SERVICE() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ROLE_HEALTH_SERVICE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> ROLE_SCREENER() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ROLE_SCREENER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<TransactionReceipt> addPaymentConfiguration(BigInteger initialPrice) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDPAYMENTCONFIGURATION, 
                Arrays.<Type>asList(new Uint32(initialPrice)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> assignToHealthCentre(String account, String healthCentreId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ASSIGNTOHEALTHCENTRE, 
                Arrays.<Type>asList(new Address(account),
                new Utf8String(healthCentreId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createHealthCentre(String healthCentreId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEHEALTHCENTRE, 
                Arrays.<Type>asList(new Utf8String(healthCentreId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getDiagnosisDetails(String diagnosisId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETDIAGNOSISDETAILS, 
                Arrays.<Type>asList(new Utf8String(diagnosisId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<byte[]> getRoleAdmin(byte[] role) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETROLEADMIN, 
                Arrays.<Type>asList(new Bytes32(role)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<String> getRoleMember(byte[] role, BigInteger index) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETROLEMEMBER, 
                Arrays.<Type>asList(new Bytes32(role),
                new Uint256(index)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getRoleMemberCount(byte[] role) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETROLEMEMBERCOUNT, 
                Arrays.<Type>asList(new Bytes32(role)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> grantRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GRANTROLE, 
                Arrays.<Type>asList(new Bytes32(role),
                new Address(account)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> hasRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_HASROLE, 
                Arrays.<Type>asList(new Bytes32(role),
                new Address(account)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> invalidateDiagnosis(String diagnosisId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INVALIDATEDIAGNOSIS, 
                Arrays.<Type>asList(new Utf8String(diagnosisId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> payReward(String diagnosisId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_PAYREWARD, 
                Arrays.<Type>asList(new Utf8String(diagnosisId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> registerDiagnosis(String diagnosisId, String healthCentreId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REGISTERDIAGNOSIS, 
                Arrays.<Type>asList(new Utf8String(diagnosisId),
                new Utf8String(healthCentreId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEROLE, 
                Arrays.<Type>asList(new Bytes32(role),
                new Address(account)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REVOKEROLE, 
                Arrays.<Type>asList(new Bytes32(role),
                new Address(account)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setScreenerConfiguration(String screener, BigInteger configurationId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETSCREENERCONFIGURATION, 
                Arrays.<Type>asList(new Address(screener),
                new Uint32(configurationId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setToken(String _token, String _owner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETTOKEN, 
                Arrays.<Type>asList(new Address(_token),
                new Address(_owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> updatePrice(BigInteger configurationId, BigInteger price) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPDATEPRICE, 
                Arrays.<Type>asList(new Uint32(configurationId),
                new Uint32(price)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> validateDiagnosis(String diagnosisId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_VALIDATEDIAGNOSIS, 
                Arrays.<Type>asList(new Utf8String(diagnosisId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Nut4Health load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Nut4Health(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Nut4Health load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Nut4Health(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Nut4Health load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Nut4Health(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Nut4Health load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Nut4Health(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Nut4Health> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Nut4Health.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Nut4Health> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Nut4Health.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Nut4Health> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Nut4Health.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Nut4Health> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Nut4Health.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class DiagnosisCreatedEventResponse extends BaseEventResponse {
        public String diagnosisId;

        public String screener;

        public BigInteger reward;
    }

    public static class DiagnosisInvalidatedEventResponse extends BaseEventResponse {
        public String diagnosisId;

        public String validator;
    }

    public static class DiagnosisPaidEventResponse extends BaseEventResponse {
        public String diagnosisId;
    }

    public static class DiagnosisValidatedEventResponse extends BaseEventResponse {
        public String diagnosisId;

        public String validator;
    }

    public static class HealthCentreCreatedEventResponse extends BaseEventResponse {
        public String healthCentreId;
    }

    public static class HealthServiceAssignedEventResponse extends BaseEventResponse {
        public String healthService;

        public String healthCentreId;
    }

    public static class PaymentUpdatedEventResponse extends BaseEventResponse {
        public BigInteger configurationId;

        public BigInteger price;
    }

    public static class RoleAdminChangedEventResponse extends BaseEventResponse {
        public byte[] role;

        public byte[] previousAdminRole;

        public byte[] newAdminRole;
    }

    public static class RoleGrantedEventResponse extends BaseEventResponse {
        public byte[] role;

        public String account;

        public String sender;
    }

    public static class RoleRevokedEventResponse extends BaseEventResponse {
        public byte[] role;

        public String account;

        public String sender;
    }

    public static class TokenUpdatedEventResponse extends BaseEventResponse {
        public String token;

        public String owner;
    }
}
