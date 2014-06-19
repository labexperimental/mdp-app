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

#import "CheckInViewController.h"
#import "UIImageView+AFNetworking.h"

// Url da imagem de centro
#define URL_IMAGE @"http://d39wqyw5lyhv7u.cloudfront.net/img/app.jpg"

@interface CheckInViewController ()
@property (weak, nonatomic) IBOutlet UILabel *lblNome;
@property (weak, nonatomic) IBOutlet UILabel *lblHora;
@property (weak, nonatomic) IBOutlet UILabel *lblLatitude;
@property (weak, nonatomic) IBOutlet UILabel *lblLongitude;
@property (weak, nonatomic) IBOutlet UIImageView *imgBackground;
@property (weak, nonatomic) IBOutlet UIButton *btnCheckIn;
@property (weak, nonatomic) IBOutlet UIProgressView *pvProgress;
@property (strong, nonatomic) NSTimer* tProgressTimer;
@property (strong, nonatomic) NSTimer* tButtonTimer;

@property (weak, nonatomic) IBOutlet UIScrollView *scroll;
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (strong, nonatomic) NSString* lat;
@property (strong, nonatomic) NSString* lng;
@property (weak, nonatomic) IBOutlet UIButton *btnParticipar;
@property (weak, nonatomic) IBOutlet UILabel *lblSuaLocalizacao;

- (IBAction)btnCheckInPressed:(id)sender;
- (IBAction)btnParticiparPressed:(UIButton *)sender;

@end

@implementation CheckInViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	
    //Verificação se o usuário está visualizando ou está cadastrado
	if (self.isVisualizar) {
		self.btnCheckIn.hidden = YES;
		self.btnParticipar.hidden = NO;
		
		self.pvProgress.hidden = YES;
	}
	else
	{
		self.lblSuaLocalizacao.hidden = YES;
		
		self.lblNome.text = [[NSUserDefaults standardUserDefaults] objectForKey:@"Apelido"];
		self.lblHora.text = [[NSUserDefaults standardUserDefaults] objectForKey:@"Data"];
		self.lblLatitude.text = [[NSUserDefaults standardUserDefaults] objectForKey:@"Lat"];
		self.lblLongitude.text = [[NSUserDefaults standardUserDefaults] objectForKey:@"Lng"];
		self.locationManager = [[CLLocationManager alloc]init];
		self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
		self.locationManager.delegate = self;
		[self.locationManager startUpdatingLocation];
        
        //Notificação para parar ou retornar a obtenção de local
		[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(stopLocation) name:@"background" object:nil];
		[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(playLocation) name:@"backgroundBack" object:nil];
		
        //Notificação caso o usuário entre ou sai de ambiente background
		[[NSNotificationCenter defaultCenter] addObserver: self
												 selector: @selector(handleEnteredBackground)
													 name: UIApplicationDidEnterBackgroundNotification
												   object: nil];
		
		[[NSNotificationCenter defaultCenter] addObserver: self
												 selector: @selector(handleBecomeActive)
													 name: UIApplicationDidBecomeActiveNotification
												   object: nil];
	}
    
    [self.imgBackground setImageWithURL:[NSURL URLWithString:URL_IMAGE]];
    
    self.scroll.contentSize = CGSizeMake(self.imgBackground.frame.size.width, self.imgBackground.frame.size.height);
	
	
}

-(void)dealloc
{
//	[super dealloc];
	
	[[NSNotificationCenter defaultCenter] removeObserver: self
											 //selector: @selector(handleEnteredBackground)
												 name: UIApplicationDidEnterBackgroundNotification
											   object: nil];
	
	[[NSNotificationCenter defaultCenter] removeObserver: self
											 //selector: @selector(handleBecomeActive)
												 name: UIApplicationDidBecomeActiveNotification
											   object: nil];
	
}

-(void)handleEnteredBackground
{
	[self stopTimers];
}

-(void)handleBecomeActive
{
	[self updateTimers];
}

-(void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
	
	if (self.isVisualizar) {
		return;
	}
	
    [self.locationManager stopUpdatingLocation];
	
	[self stopTimers];
	
	NSLog(@"view will disapear!!!");
}

-(void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	
	if (self.isVisualizar) {
		return;
	}
	
	[self updateTimers];
	
	NSLog(@"view will appear!!!");
}

-(void)stopTimers
{
	if (self.tButtonTimer != nil) {
		[self.tButtonTimer invalidate];
		self.tButtonTimer = nil;
	}
	
	if (self.tProgressTimer != nil) {
		[self.tProgressTimer invalidate];
		self.tProgressTimer = nil;
	}
}

//Atualiza o timer
-(void)updateTimers
{
	if (self.isVisualizar) {
		return;
	}
	
	if (self.tProgressTimer != nil || self.tButtonTimer != nil) {
		return;
	}
	
	float seconds = [CheckInService getDiffTimeInSeconds:[[NSDate alloc] init] ];
	float secRest = 60.0f*5.0f - seconds;
	
	if (secRest > 0) {
		[self.btnCheckIn setEnabled:FALSE];
		self.tButtonTimer = [NSTimer scheduledTimerWithTimeInterval:(secRest) target:self selector:@selector(enableButton) userInfo:nil repeats:NO];
		
		self.pvProgress.progress = seconds/(60.0f*5.0f);
		self.tProgressTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(updateProgressView) userInfo:nil repeats:YES];
	}
	else
	{
		[self.btnCheckIn setEnabled:TRUE];
		self.pvProgress.progress = 1;
	}
}

#pragma mark - Botões da tela

- (IBAction)btnCheckInPressed:(id)sender {
    CheckInService* service = [[CheckInService alloc] init];
    service.delegate = self;
    [service makeCheckInWithId:[[[NSUserDefaults standardUserDefaults] objectForKey:@"IDUser"] intValue] lat:self.lat lng:self.lng withIDIbge:[[NSUserDefaults standardUserDefaults] objectForKey:@"IBGE"]];
    self.btnCheckIn.enabled = NO;
}

- (IBAction)btnParticiparPressed:(UIButton *)sender {
	
	[self.navigationController popViewControllerAnimated:YES];
    NSString *appDomain = [[NSBundle mainBundle] bundleIdentifier];
    [[NSUserDefaults standardUserDefaults] removePersistentDomainForName:appDomain];
	
}

#pragma mark - Zoom

-(UIView *) viewForZoomingInScrollView:(UIScrollView *)scrollView{
    return self.imgBackground;
}
-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [self.locationManager startUpdatingLocation];
	
	NSLog(@"view did disapear!!!");
}

-(void)stopLocation{
    
    [self.locationManager stopUpdatingLocation];
}

-(void)playLocation{
    
    [self.locationManager startUpdatingLocation];
}

-(void)enableButton{
    self.btnCheckIn.enabled = YES;
    
	self.pvProgress.progress = 1;
	
	if (self.tProgressTimer != nil) {
		[self.tProgressTimer invalidate];
		self.tProgressTimer = nil;
	}
}

#pragma mark - CLLocationDelegate

- (void)locationManager:(CLLocationManager *)manager
    didUpdateToLocation:(CLLocation *)newLocation
           fromLocation:(CLLocation *)oldLocation {
    self.lat = [NSString stringWithFormat: @"%f" ,newLocation.coordinate.latitude ];
    self.lng = [NSString stringWithFormat: @"%f" ,newLocation.coordinate.longitude ];
}

- (void)locationManager:(CLLocationManager *)manager
       didFailWithError:(NSError *)error {
}
#pragma mark - check Delegate

-(void)checkInService:(CheckInService *)checkInService withSucess:(BOOL)sucess{
    if(sucess){
        
        //Atualiza user defaults
        NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
        [prefs setObject:self.lat forKey:@"Lat"];
        [prefs setObject:self.lng forKey:@"Lng"];
        NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
        [dateFormat setLocale:[NSLocale systemLocale]];
        [dateFormat setDateFormat:@"dd/MM HH:mm"];
        [prefs setObject:[dateFormat stringFromDate: [NSDate date]] forKey:@"Data"];
        [prefs synchronize];
        
        //Atualiza textos
        self.lblNome.text = [[NSUserDefaults standardUserDefaults] objectForKey:@"Apelido"];
        self.lblHora.text = [[NSUserDefaults standardUserDefaults] objectForKey:@"Data"];
        self.lblLatitude.text = [[NSUserDefaults standardUserDefaults] objectForKey:@"Lat"];
        self.lblLongitude.text = [[NSUserDefaults standardUserDefaults] objectForKey:@"Lng"];
        
        self.tButtonTimer = [NSTimer scheduledTimerWithTimeInterval:60*5 target:self selector:@selector(enableButton) userInfo:nil repeats:NO];
		
		self.pvProgress.progress = 0;
		self.tProgressTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(updateProgressView) userInfo:nil repeats:YES];
		
		 [[[UIAlertView alloc] initWithTitle:@"Check In" message:@"Dados enviados com sucesso! Por favor aguarde 5 minutos para realizar um novo checkin." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
		
		[self.imgBackground setImageWithURL:[NSURL URLWithString:URL_IMAGE]];
		
		[CheckInService setTime:[[NSDate alloc] init] ];
		
    }else{
        self.btnCheckIn.enabled = YES;
        
        [[[UIAlertView alloc] initWithTitle:@"Check In" message:@"Falha ao efetuar o check-in, por favor tente de novo." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    }
}

//Atualiza progress view

-(void)updateProgressView
{
	self.pvProgress.progress += 1.0f/(60.0f*5.0f);
	
	if (self.pvProgress.progress >=1) {
		[self.tProgressTimer invalidate];
		self.tProgressTimer = nil;
	}
	
	NSLog(@"count!!");
}

@end
