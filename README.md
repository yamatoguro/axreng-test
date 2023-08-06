<h2 align="center">AXRENG - BackEnd Challenge</h2>

<div align="center">
<p align="center"> This is a web crawler challenge project made to ensure knowledge and skills among all needed to build it without changing the dependecies and making it run on docker.
    <br> 
</p>

At first I made an approach to id generation using Math library to generate it as randomly as possible with my knowledge
Then it was time to make the first steps on crawler, starting by the service class, which is responsible by managing any proccess coming from endpoints and calling the Crawler methods to start and manage the threads (using Runnables), making it possible to have multiple processess running at the same time.

## The Crawler

At first moment I was a bit lost into this subject, but soon enough I found a way to build it using what I learned on past projects. I based my crawler on one of those projects. Since this I changed a lot of the code while optimizing it to run smoothly while running multiple searches at same time.
At last I built the crawler appart of service to ensure it's not responsible for any thread or any management which is made by Service class.

## Service Class

This one is responsible for creating and assigning the processes into the threads and making it accessible enough to get a partial result anytime. By doing this, it is responsible to not interrupt and keep safe any partial result from running processes.

## Automated testing

I made some unit tests using Junit and Mockito. This is a part that I have less experience, and I've made what I could to keep it a good approach as testing each case individually. I don't really know if this is the best practice to testing this application, but I've done it with all I know.

### Conclusion

This challenge made me learn a lot along the development and this was an awesome experience for me.

Thank you for your time and for this oportunity.
Iago Faria dos Reis
</div>
