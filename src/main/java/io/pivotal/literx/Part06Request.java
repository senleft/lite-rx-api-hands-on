package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import io.pivotal.literx.repository.ReactiveRepository;
import io.pivotal.literx.repository.ReactiveUserRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static io.pivotal.literx.domain.User.JESSE;
import static io.pivotal.literx.domain.User.SKYLER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Learn how to control the demand.
 *
 * @author Sebastien Deleuze
 */
public class Part06Request {

	ReactiveRepository<User> repository = new ReactiveUserRepository();

//========================================================================================

	// TODO Create a StepVerifier that expect 4 values to be received and requests all values
	StepVerifier requestAllExpectFour(Flux<User> flux) {
		return StepVerifier.create(flux)
				.expectNextCount(4).expectComplete();
	}

//========================================================================================

	// TODO Create a StepVerifier that initially requests 1 value and expects User.SKYLER then requests another value and expects User.JESSE then stops request.
	StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {
		return StepVerifier.create(flux)
				.assertNext(user -> assertThat(user).isEqualTo(SKYLER))
				.assertNext(user -> assertThat(user).isEqualTo(JESSE))
				.thenCancel();
	}

//========================================================================================

	// TODO Return a Flux with all users stored in the repository that prints automatically logs for all Reactive Streams signals
	Flux<User> fluxWithLog() {
		return Flux.from(repository.findAll())
				.log();
	}

//========================================================================================

	// TODO Return a Flux with all users stored in the repository that prints "Starting:" on subscribe, "firstname lastname" for all values and "The end!" on complete
	Flux<User> fluxWithDoOnPrintln() {
		return Flux.from(repository.findAll())
				.doOnSubscribe(subscription -> System.out.println("Starting " + subscription))
				.doOnNext(user -> {
					System.out.println(String.format("%s %s", user.getFirstname(), user.getLastname()));
				}).doOnComplete(() -> System.out.println("Completed"));
	}

}
