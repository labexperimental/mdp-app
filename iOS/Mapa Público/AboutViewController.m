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


#import "AboutViewController.h"

@interface AboutViewController ()
@property (weak, nonatomic) IBOutlet UIScrollView *scroll;
@property (weak, nonatomic) IBOutlet UIButton *btnLougout;

- (IBAction)btnVoltarPressed:(UIButton *)sender;
- (IBAction)btnLogoutPressed:(UIButton *)sender;

@end

@implementation AboutViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.scroll setContentSize:CGSizeMake(320, 3125)];
    if(![[NSUserDefaults standardUserDefaults] objectForKey:@"IDUser"]){
        self.btnLougout.hidden = YES;
    }else{
        self.btnLougout.hidden = NO;
        
    }
}


- (IBAction)btnVoltarPressed:(UIButton *)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)btnLogoutPressed:(UIButton *)sender {
    [self.navigationController popToRootViewControllerAnimated:YES];
    NSString *appDomain = [[NSBundle mainBundle] bundleIdentifier];
    [[NSUserDefaults standardUserDefaults] removePersistentDomainForName:appDomain];
}
@end
