package webshopREST.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import types.AuthenticationToken;
import types.User;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

public class TokenService {
	private static String SECRET = "testi";
	
	/** Creates token for given user
	 * @param user for whom token is created
	 * @return created token
	 * @throws UnsupportedEncodingException if something goes wrong with encoding.
	 * @throws NoSuchAlgorithmException if there's something wrong with signing algorithm
	 * @throws InvalidKeyException  if there's something wrong with secret key used for signing
	 */
	public static AuthenticationToken getToken(User user) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
		//TODO: Tokenin luominen.
		//Header
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode headerNode = mapper.createObjectNode();
		headerNode.put("alg", "HS256");
		headerNode.put("typ", "JWT");
		String header = headerNode.toString();
		String headerB64 = encodeToB64(header);
		System.out.println("header: " + header);
		
		//Payload
		Date nyt = new Date();
		long kesto = 3600000; //1 hour in ms
		
		ArrayNode rolesNode = mapper.createArrayNode();
		for (String role: user.getRoles()) {
			rolesNode.add(role);
		}
		ObjectNode payloadNode = mapper.createObjectNode();
		payloadNode.put("sub", user.getId());
		payloadNode.set("roles", rolesNode);
		payloadNode.put("iat", nyt.getTime());
		payloadNode.put("exp", nyt.getTime() + kesto);
		String payload = payloadNode.toString();
		String payloadB64 = encodeToB64(payload);
		System.out.println("payload: " + payload);		
		
		// Signature
		String toBesigned = headerB64 + "." + payloadB64;
		//String signature = DigestUtils.sha256Hex(toBesigned);
		//String signature = new String(DigestUtils.sha256(toBesigned.getBytes()));
		String signature = createSignature(toBesigned);
		System.out.println("toBeSigned: " + toBesigned);
		System.out.println("signature: " + signature);
		
		//Result
		AuthenticationToken token = new AuthenticationToken();
		token.setToken(headerB64 + "." + payloadB64 + "." + signature);
		return token;
	}
	
	/** Encodes given string to Base64Url string
	 * @param mjono String to be encoded
	 * @return encoded string as string
	 * @throws UnsupportedEncodingException
	 */
	private static String encodeToB64(String mjono) throws UnsupportedEncodingException {
		return Base64.getEncoder().encodeToString(mjono.getBytes());
	}
	
	/**
	 * @param mjono String to be signed
	 * @return Signature for the given string
	 * @throws NoSuchAlgorithmException if algorithm is wrong
	 * @throws InvalidKeyException if there's something wrong with the key
	 */
	private static String createSignature(String mjono) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] result = null;
		String algorithm = "HmacSHA256";
		Mac mac = Mac.getInstance(algorithm);
		SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(), algorithm);
		mac.init(secretKeySpec);
		result = mac.doFinal(mjono.getBytes());
		
		return new String(result);
	}
}
