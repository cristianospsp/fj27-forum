package br.com.alura.forum.service;

import br.com.alura.forum.model.User;
import br.com.alura.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> byEmail = userRepository.findByEmail(username);

		return byEmail.orElseThrow(() -> new UsernameNotFoundException("Nao foi possivel encontrar o usuario com email: " + username));

	}


	public UserDetails loadUserById(Long userId) {
		Optional<User> possibleUser = userRepository.findById(userId);
		return possibleUser.orElseThrow(
				() -> new UsernameNotFoundException("Não foi possível encontrar o usuário com id: " +
						userId));
	}


}
