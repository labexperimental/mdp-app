//The MIT License (MIT)
//
//Copyright (c) 2014 LittleBoat
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.
//
//www.littleboat.com.br


#import "AsyncTask.h"
#import "Reachability.h"
#import "Util.h"

@implementation AsyncTask


-(void)getJsonFromUrl:(NSString *)url httpMethod:(NSString *)method withPostString:(NSDictionary *)dic{
    
    if([[Reachability  reachabilityForInternetConnection] currentReachabilityStatus] == NotReachable){
        [[[UIAlertView alloc] initWithTitle:@"Sem Internet" message:@"Não foi encontrado nenhum sinal de internet, por favor conecte-se a uma rede." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil] show ];
        [self.delegate asyncTask:self didLoadDictionary:nil];
        return;
    }
    NSURL* path = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", ABSOLUTE_URL , url]];
    
    NSMutableURLRequest* requestApi = [NSMutableURLRequest requestWithURL:path];
    [requestApi setTimeoutInterval:250];
    [requestApi setHTTPMethod:method];
    
    if(dic){
        NSArray* keys = [dic allKeys];
        
        NSString* postString = nil;
        
        
        for(int i = 0; i < [keys count]; i++){
            NSString* key = [keys objectAtIndex:i];
            if([[dic objectForKey:key] isKindOfClass:[NSArray class]]){
                NSArray* a = [dic objectForKey:key];
                for(int i = 0; i < [a count]; i++){
                    if(postString){
                        postString = [NSString stringWithFormat:@"%@&%@[]=%@", postString, key, [a objectAtIndex:i]];
                    }else{
                        postString = [NSString stringWithFormat:@"%@[]=%@", key, [a objectAtIndex:0]];
                    }
                }
            }else{
                if(postString){
                    postString = [NSString stringWithFormat:@"%@&%@=%@", postString, key, [dic objectForKey:key]];
                }else{
                    postString = [NSString stringWithFormat:@"%@=%@", [keys objectAtIndex:0], [dic objectForKey:[keys objectAtIndex:0]]];
                }
            }
            
        }
        
//        @"CriteriaTitle[]=VALOR&CriteriaTitle[]=Valor"
        
        NSData* postStringData = [postString dataUsingEncoding:NSUTF8StringEncoding];
        [requestApi setHTTPBody:postStringData];
    }
    
    [requestApi setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [NSURLConnection sendAsynchronousRequest:requestApi
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
                               if(error == 0){
                                   //NSString* string = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
                                   NSDictionary * resultado = [NSJSONSerialization
                                                               JSONObjectWithData:data
                                                               options:NSJSONReadingAllowFragments
                                                               error:&error];
                                   [self.delegate asyncTask:self didLoadDictionary:resultado];
                               }else{
                                   [self.delegate asyncTask:self didLoadDictionary:nil];
                               }
    }];
}


@end
