package com.seniors.domain.auth.service;

import com.seniors.domain.auth.common.OAuthApiClient;
import com.seniors.domain.auth.common.OAuthInfoResponse;
import com.seniors.domain.auth.common.OAuthLoginParams;
import com.seniors.common.constant.OAuthProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RequestOAuthInfoService {
	private final Map<OAuthProvider, OAuthApiClient> clients = new HashMap<>();

	public RequestOAuthInfoService(List<OAuthApiClient> clients) {
		if (clients == null) {
			throw new IllegalArgumentException("Clients cannot be null");
		}

		for (OAuthApiClient client : clients) {
			this.clients.put(client.oAuthProvider(), client);
		}
	}

	public OAuthInfoResponse request(OAuthLoginParams params) {
		if (params == null || params.oAuthProvider() == null) {
			throw new IllegalArgumentException("OAuth provider must be specified");
		}

		OAuthApiClient client = clients.get(params.oAuthProvider());
		if (client == null) {
			throw new IllegalArgumentException("No client found for the given OAuth provider");
		}

		String accessToken = client.requestAccessToken(params);
		return client.requestOAuthInfo(accessToken);
	}
}
