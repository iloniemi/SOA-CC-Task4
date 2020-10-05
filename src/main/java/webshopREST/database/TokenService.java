package webshopREST.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import types.AuthenticationToken;
import types.User;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

public class TokenService {
	private static String SECRET = "testi";

	/**
	 * Creates token for given user
	 * 
	 * @param user for whom token is created
	 * @return created token
	 * @throws UnsupportedEncodingException if something goes wrong with encoding.
	 * @throws NoSuchAlgorithmException     if there's something wrong with signing
	 *                                      algorithm
	 * @throws InvalidKeyException          if there's something wrong with secret
	 *                                      key used for signing
	 */
	public static AuthenticationToken getToken(User user)
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
		// TODO: Tokenin luominen.
		// Header
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode headerNode = mapper.createObjectNode();
		headerNode.put("alg", "HS256");
		headerNode.put("typ", "JWT");
		String header = headerNode.toString();
		String headerB64 = encodeToB64(header);
		System.out.println("header: " + header);

		// Payload
		Date nyt = new Date();
		long kesto = 3600000; // 1 hour in ms

		ArrayNode rolesNode = mapper.createArrayNode();
		for (String role : user.getRoles()) {
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
		// String signature = DigestUtils.sha256Hex(toBesigned);
		// String signature = new String(DigestUtils.sha256(toBesigned.getBytes()));
		String signature = createSignature(toBesigned, SECRET);
		System.out.println("toBeSigned: " + toBesigned);
		System.out.println("signature: " + signature);

		// Result
		AuthenticationToken token = new AuthenticationToken();
		token.setToken(headerB64 + "." + payloadB64 + "." + signature);
		return token;
	}

	/**
	 * Encodes given string to Base64Url string
	 * 
	 * @param mjono String to be encoded
	 * @return encoded string as string
	 * @throws UnsupportedEncodingException
	 */
	private static String encodeToB64(String mjono) throws UnsupportedEncodingException {
		return Base64.getUrlEncoder().encodeToString(mjono.getBytes());
	}
	
	/**
	 * Decodes given string from Base64Url string
	 * 
	 * @param mjono String to be decoded
	 * @return decoded String
	 */
	private static String decodeFromB64(String mjono) {
		return new String(Base64.getUrlDecoder().decode(mjono));
	}
	
	
	private static String createSignature(String mjono, String secret) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
		return encodeToB64(HmacSHA256(mjono, secret));
	}

	/**
	 * @param mjono String to be hashed
	 * @return HmacSHA256-hash of the given string
	 * @throws NoSuchAlgorithmException if algorithm is wrong
	 * @throws InvalidKeyException      if there's something wrong with the key
	 * @throws UnsupportedEncodingException   if the encoding to B64 fails
	 */
	private static String HmacSHA256(String mjono, String secret) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		byte[] result = null;
		String algorithm = "HmacSHA256";
		Mac mac = Mac.getInstance(algorithm);
		SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), algorithm);
		mac.init(secretKeySpec);
		result = mac.doFinal(mjono.getBytes());

		return  new String(result);
	}
	
	
	/**
	 * Tries to create  JWT-token with the known secret key and checks if the token matches the sent message
	 * 
	 * @param message JWT-token to check
	 * @return is the token valid
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public static Boolean checkTokenValidity(String payload, String signature) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode headerNode = mapper.createObjectNode();
		headerNode.put("alg", "HS256");
		headerNode.put("typ", "JWT");
		String header = headerNode.toString();

		String comparableSignature = createSignature(encodeToB64(header) + "." + encodeToB64(payload), SECRET);
		signature = encodeToB64(signature);
		
		Boolean result = signature.equals(comparableSignature);
		
		System.out.println("Jos allekirjoitukset ovat samat, token on autorisoitu: " + result);
		System.out.println(signature);
		System.out.println(comparableSignature);
				
		return result;
	}
	

}
