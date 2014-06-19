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


#import "CheckInService.h"

@implementation CheckInService

//Efetua check in
-(void)makeCheckInWithId:(int)userId lat:(NSString *)lat lng:(NSString *)lng withIDIbge: (NSString*) ibge{
    AsyncTask *task = [[AsyncTask alloc] init];
    task.delegate = self;
    [task getJsonFromUrl:[NSString stringWithFormat:@"checkin.php?id=%d&lat=%@&lon=%@&origem=%@", userId, lat, lng, ibge] httpMethod:@"GET" withPostString:nil];
}

//Retorno da classe AsyncTask apos resolução do webService
-(void)asyncTask:(AsyncTask *)asyncTask didLoadDictionary:(NSDictionary *)dic{
    NSLog(@"%@", dic);
    if(dic && [[dic objectForKey:@"status"] isEqualToString:@"ok"]){
        [self.delegate checkInService:self withSucess:YES];
        
    }else{
        [self.delegate checkInService:self withSucess:NO];

    }
}

//metodo que adiciona no user defaults o tempo do check in
+(void)setTime:(NSDate*)time
{
	[[NSUserDefaults standardUserDefaults] setObject:time forKey:@"lastCheckin"];
}

//metodo para ver diferença de tempo
+(float)getDiffTimeInSeconds:(NSDate*)time
{
	NSDate* lastCheckin = [[NSUserDefaults standardUserDefaults] objectForKey:@"lastCheckin"];
	
	if (lastCheckin == nil) {
		return 10*60.0f;
	}
	
	NSTimeInterval interval = [time timeIntervalSinceDate:lastCheckin];
	NSLog(@"%f",interval);
	
	return interval;
}

@end
