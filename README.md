# SampleAtnosEff
Sample codes for [this slides](https://speakerdeck.com/hiroki6/extensible-effects-beyond-the-monad-transformers).

## examples
- mtls  
Implementing monad stack with monad transformers in [cats](https://github.com/typelevel/cats).

- effs  
Implementing monad stack with [atnos-eff](https://github.com/atnos-org/eff).

## app
Sample codes of Layered Architecture with [atnos-eff](https://github.com/atnos-org/eff).

This application is providing only one api to create post.

### How to use
- migrations  
Please use DDL.sql and DML.sql in databases directory.

- running application  
```
sbt run
```

- create post  
```
POST /posts/create

{
  "user_id": "D8E265B7-AF8F-4F2C-AEEF-2430CC609417",
  "text": "Enjoy eff!!"
}
```
