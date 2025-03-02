


```shell
docker run --rm  \
    -it \
    -v postgresml_data:/var/lib/postgresql \
    -p 5433:6432 \
    -p 8000:8000 \
    ghcr.io/postgresml/postgresml:2.10.0 \
    sudo -u postgresml psql -d postgresml
```



--------------------

Text Summarization

```shell
SELECT pgml.transform(
        task => '{
          "task": "summarization", 
          "model": "google/pegasus-xsum"
    }'::JSONB,
        inputs => array[
         'Paris is the capital and most populous city of France, with an estimated population of 2,175,601 residents as of 2018,
         in an area of more than 105 square kilometres (41 square miles). The City of Paris is the centre and seat of government
         of the region and province of Île-de-France, or Paris Region, which has an estimated population of 12,174,880,
         or about 18 percent of the population of France as of 2017.'
        ]
);
```

```shell
select summary_text->0->'summary_text' from (SELECT pgml.transform(
task => '{
"task": "summarization",
"model": "google/pegasus-xsum"
}'::JSONB,
inputs => array[
'If I speak in the tongues of men and of angels, but have not love, I am a noisy gong or a clanging cymbal. And if I have prophetic powers, and understand all mysteries and all knowledge, and if I have all faith, so as to remove mountains, but have not love, I am nothing. If I give away all I have, and if I deliver up my body to be burned, but have not love, I gain nothing. Love is patient and kind; love does not envy or boast; it is not arrogant or rude. It does not insist on its own way; it is not irritable or resentful; it does not rejoice at wrongdoing, but rejoices with the truth. Love bears all things, believes all things, hopes all things, endures all things. Love never ends. As for prophecies, they will pass away; as for tongues, they will cease; as for knowledge, it will pass away. For we know in part and we prophesy in part, 10 but when the perfect comes, the partial will pass away. When I was a child, I spoke like a child, I thought like a child, I reasoned like a child. When I became a man, I gave up childish ways. For now we see in a mirror dimly, but then face to face. Now I know in part; then I shall know fully, even as I have been fully known. So now faith, hope, and love abide, these three; but the greatest of these is love.'
]
) as summary_text) summary_text;
```


Text Classification


```shell
SELECT pgml.transform( task   => 'text-classification', inputs => ARRAY['I love how amazingly simple ML has become!']) AS positivity;
```

Text classification

```shell
select positivity->0->'label' from (SELECT pgml.transform( task   => 'text-classification', inputs => ARRAY['I hate doing mundane and thankless tasks. ☹️']) as positivity) positivity;
```
